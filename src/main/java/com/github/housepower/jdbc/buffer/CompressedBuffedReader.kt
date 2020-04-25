package com.github.housepower.jdbc.buffer

import net.jpountz.lz4.LZ4Factory
import java.io.IOException
import kotlin.experimental.and
import kotlin.experimental.or

class CompressedBuffedReader(private val buf: BuffedReader) : BuffedReader {
    private var position = 0
    private var capacity = 0
    private var decompressed: ByteArray? = null
    private val lz4FastDecompressor = LZ4Factory.safeInstance().fastDecompressor()

    override suspend fun readBinary(): Long {
        if (position == capacity) {
            decompressed = readCompressedData()
            position = 0
            capacity = decompressed!!.size
        }
        return decompressed!![position++].toLong()
    }

    override suspend fun readBinary(bytes: ByteArray): Int {
        var i = 0
        while (i < bytes.size) {
            if (position == capacity) {
                decompressed = readCompressedData()
                position = 0
                capacity = decompressed!!.size
            }
            val pending = bytes.size - i
            val fillLength = Math.min(pending, capacity - position)
            if (fillLength > 0) {
                System.arraycopy(decompressed, position, bytes, i, fillLength)
                i += fillLength
                position += fillLength
            }
        }
        return bytes.size
    }

    @Throws(IOException::class)
    private suspend fun readCompressedData(): ByteArray {
        //TODO: validate checksum
        buf.readBinary(ByteArray(16))
        val compressedHeader = ByteArray(9)
        if (buf.readBinary(compressedHeader) != 9) {
            //TODO:more detail for exception
            throw IOException("")
        }
        val method = unsignedByte(compressedHeader[0])
        val compressedSize = readInt(compressedHeader, 1)
        val decompressedSize = readInt(compressedHeader, 5)
        return when (method) {
            LZ4 -> readLZ4CompressedData(compressedSize - 9, decompressedSize)
            NONE -> readNoneCompressedData(decompressedSize)
            else -> throw UnsupportedOperationException("Unknown compression method: $method")
        }
    }

    suspend private fun readNoneCompressedData(size: Int): ByteArray {
        val decompressed = ByteArray(size)
        if (buf.readBinary(decompressed) != size) {
            throw IOException("Cannot decompress use None method.")
        }
        return decompressed
    }

    private suspend fun readLZ4CompressedData(compressedSize: Int, decompressedSize: Int): ByteArray {
        val compressed = ByteArray(compressedSize)
        if (buf.readBinary(compressed) == compressedSize) {
            val decompressed = ByteArray(decompressedSize)
            if (lz4FastDecompressor.decompress(compressed, decompressed) == compressedSize) {
                return decompressed
            }
        }
        throw IOException("Cannot decompress use LZ4 method.")
    }

    private fun unsignedByte(byt: Byte): Int {
        return 0x0FF and byt.toInt()
    }

    private fun readInt(bytes: ByteArray, begin: Int): Int {
        return (bytes[begin] and 0xFF.toByte()).toInt() or
                ((bytes[begin + 1] and 0XFF.toByte()).toInt() shl 8) or
                ((bytes[begin + 2] and 0xFF.toByte()).toInt() shl 16) or
                ((0xFF and bytes[begin + 3].toInt()) shl 24)
    }

    companion object {
        private const val LZ4 = 0x82
        private const val NONE = 0x02
        private const val ZSTD = 0x90
    }

}