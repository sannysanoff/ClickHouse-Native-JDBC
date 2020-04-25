/*
 * Copyright 2017 YANDEX LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2012 tamtam180
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.housepower.jdbc.misc

/**
 * @author tamtam180 - kirscheless at gmail.com
 * @see http://google-opensource.blogspot.jp/2011/04/introducing-cityhash.html
 *
 * @see http://code.google.com/p/cityhash/
 */
/**
 * NOTE: The code is modified to be compatible with CityHash128 used in ClickHouse
 */
object ClickHouseCityHash {
    private const val k0 = -0x3c5a37a36834ced9L
    private const val k1 = -0x4b6d499041670d8dL
    private const val k2 = -0x651e95c4d06fbfb1L
    private const val k3 = -0x36b62838af619aa9L
    private fun toLongLE(b: ByteArray, i: Int): Long {
        return (b[i + 7].toLong() shl 56) +
                ((b[i + 6].toLong() and 0xFFL) shl 48) +
                ((b[i + 5].toLong() and 0xFFL) shl 40) +
                ((b[i + 4].toLong() and 0xFFL) shl 32) +
                ((b[i + 3].toLong() and 0xFFL) shl 24) +
                ((b[i + 2].toLong() and 0xFFL) shl 16) +
                ((b[i + 1].toLong() and 0xFFL) shl 8) +
                ((b[i + 0].toLong() and 0xFFL) shl 0)
    }

    private fun toIntLE(b: ByteArray, i: Int): Long {
        return ((b[i + 3].toLong() and 0xFFL) shl 24) + ((b[i + 2].toLong() and 0xFFL) shl 16) + ((b[i + 1].toLong() and 255) shl 8) + ((b[i + 0].toLong() and 255L) shl 0)
    }

    private fun fetch64(s: ByteArray, pos: Int): Long {
        return toLongLE(s, pos)
    }

    private fun fetch32(s: ByteArray, pos: Int): Long {
        return toIntLE(s, pos)
    }

    private fun rotate(`val`: Long, shift: Int): Long {
        return if (shift == 0) `val` else `val` ushr shift or (`val` shl 64 - shift)
    }

    private fun rotateByAtLeast1(`val`: Long, shift: Int): Long {
        return `val` ushr shift or (`val` shl 64 - shift)
    }

    private fun shiftMix(`val`: Long): Long {
        return `val` xor (`val` ushr 47)
    }

    private const val kMul = -0x622015f714c7d297L
    private fun hash128to64(u: Long, v: Long): Long {
        var a = (u xor v) * kMul
        a = a xor (a ushr 47)
        var b = (v xor a) * kMul
        b = b xor (b ushr 47)
        b *= kMul
        return b
    }

    private fun hashLen16(u: Long, v: Long): Long {
        return hash128to64(u, v)
    }

    private fun hashLen0to16(s: ByteArray, pos: Int, len: Int): Long {
        if (len > 8) {
            val a = fetch64(s, pos + 0)
            val b = fetch64(s, pos + len - 8)
            return hashLen16(a, rotateByAtLeast1(b + len, len)) xor b
        }
        if (len >= 4) {
            val a = fetch32(s, pos + 0)
            return hashLen16((a shl 3) + len, fetch32(s, pos + len - 4))
        }
        if (len > 0) {
            val a = s[pos + 0]
            val b = s[pos + (len ushr 1)]
            val c = s[pos + len - 1]
            val y = a.toInt() + (b.toInt() shl 8)
            val z = len + (c.toInt() shl 2)
            return shiftMix(y * k2 xor z * k3) * k2
        }
        return k2
    }

    private fun weakHashLen32WithSeeds(
            w: Long, x: Long, y: Long, z: Long,
            a: Long, b: Long): LongArray {
        var a = a
        var b = b
        a += w
        b = rotate(b + a + z, 21)
        val c = a
        a += x
        a += y
        b += rotate(a, 44)
        return longArrayOf(a + z, b + c)
    }

    private fun weakHashLen32WithSeeds(s: ByteArray, pos: Int, a: Long, b: Long): LongArray {
        return weakHashLen32WithSeeds(
                fetch64(s, pos + 0),
                fetch64(s, pos + 8),
                fetch64(s, pos + 16),
                fetch64(s, pos + 24),
                a,
                b
        )
    }

    private fun cityMurmur(s: ByteArray, pos: Int, len: Int, seed0: Long, seed1: Long): LongArray {
        var pos = pos
        var a = seed0
        var b = seed1
        var c: Long = 0
        var d: Long = 0
        var l = len - 16
        if (l <= 0) {
            a = shiftMix(a * k1) * k1
            c = b * k1 + hashLen0to16(s, pos, len)
            d = shiftMix(a + if (len >= 8) fetch64(s, pos + 0) else c)
        } else {
            c = hashLen16(fetch64(s, pos + len - 8) + k1, a)
            d = hashLen16(b + len, c + fetch64(s, pos + len - 16))
            a += d
            do {
                a = a xor shiftMix(fetch64(s, pos + 0) * k1) * k1
                a *= k1
                b = b xor a
                c = c xor shiftMix(fetch64(s, pos + 8) * k1) * k1
                c *= k1
                d = d xor c
                pos += 16
                l -= 16
            } while (l > 0)
        }
        a = hashLen16(a, c)
        b = hashLen16(d, b)
        return longArrayOf(a xor b, hashLen16(b, a))
    }

    private fun cityHash128WithSeed(s: ByteArray, pos: Int, len: Int, seed0: Long, seed1: Long): LongArray {
        var pos = pos
        var len = len
        if (len < 128) {
            return cityMurmur(s, pos, len, seed0, seed1)
        }
        var v = LongArray(2)
        var w = LongArray(2)
        var x = seed0
        var y = seed1
        var z = k1 * len
        v[0] = rotate(y xor k1, 49) * k1 + fetch64(s, pos)
        v[1] = rotate(v[0], 42) * k1 + fetch64(s, pos + 8)
        w[0] = rotate(y + z, 35) * k1 + x
        w[1] = rotate(x + fetch64(s, pos + 88), 53) * k1

        // This is the same inner loop as CityHash64(), manually unrolled.
        do {
            x = rotate(x + y + v[0] + fetch64(s, pos + 16), 37) * k1
            y = rotate(y + v[1] + fetch64(s, pos + 48), 42) * k1
            x = x xor w[1]
            y = y xor v[0]
            z = rotate(z xor w[0], 33)
            v = weakHashLen32WithSeeds(s, pos, v[1] * k1, x + w[0])
            w = weakHashLen32WithSeeds(s, pos + 32, z + w[1], y)
            run {
                val swap = z
                z = x
                x = swap
            }
            pos += 64
            x = rotate(x + y + v[0] + fetch64(s, pos + 16), 37) * k1
            y = rotate(y + v[1] + fetch64(s, pos + 48), 42) * k1
            x = x xor w[1]
            y = y xor v[0]
            z = rotate(z xor w[0], 33)
            v = weakHashLen32WithSeeds(s, pos, v[1] * k1, x + w[0])
            w = weakHashLen32WithSeeds(s, pos + 32, z + w[1], y)
            run {
                val swap = z
                z = x
                x = swap
            }
            pos += 64
            len -= 128
        } while (len >= 128)
        y += rotate(w[0], 37) * k0 + z
        x += rotate(v[0] + z, 49) * k0

        // If 0 < len < 128, hash up to 4 chunks of 32 bytes each from the end of s.
        var tail_done = 0
        while (tail_done < len) {
            tail_done += 32
            y = rotate(y - x, 42) * k0 + v[1]
            w[0] += fetch64(s, pos + len - tail_done + 16)
            x = rotate(x, 49) * k0 + w[0]
            w[0] += v[0]
            v = weakHashLen32WithSeeds(s, pos + len - tail_done, v[0], v[1])
        }

        // At this point our 48 bytes of state should contain more than
        // enough information for a strong 128-bit hash.  We use two
        // different 48-byte-to-8-byte hashes to get a 16-byte final result.
        x = hashLen16(x, v[0])
        y = hashLen16(y, w[0])
        return longArrayOf(
                hashLen16(x + v[1], w[1]) + y,
                hashLen16(x + w[1], y + v[1])
        )
    }

    fun cityHash128(s: ByteArray, pos: Int, len: Int): LongArray {
        return if (len >= 16) {
            cityHash128WithSeed(
                    s, pos + 16,
                    len - 16,
                    fetch64(s, pos) xor k3,
                    fetch64(s, pos + 8)
            )
        } else if (len >= 8) {
            cityHash128WithSeed(ByteArray(0), 0, 0,
                    fetch64(s, pos) xor len * k0,
                    fetch64(s, pos + len - 8) xor k1
            )
        } else {
            cityHash128WithSeed(s, pos, len, k0, k1)
        }
    }
}