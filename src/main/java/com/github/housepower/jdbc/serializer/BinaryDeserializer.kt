package com.github.housepower.jdbc.serializer

import com.github.housepower.jdbc.buffer.BuffedReader
import com.github.housepower.jdbc.buffer.SocketBuffedReader
import com.github.housepower.jdbc.misc.Container
import io.ktor.network.sockets.Socket

class BinaryDeserializer(socket: Socket?) {
    private val container: Container<BuffedReader>

    suspend fun readVarInt(): Long {
        var number = 0L
        val cg = container.get()
        for (i in 0..8) {
            val byt = cg.readBinary()
            number = number or ((byt and 0x7FL) shl (7 * i))
            if (byt and 0x80L == 0L) {
                break
            }
        }
        return number
    }


    suspend fun readShort(): Short {
        return ((container.get().readBinary() and 0xFF) + (container.get().readBinary() and 0xFF shl 8)).toShort()
    }


    val ba4 = ByteArray(4)

    suspend fun readInt(): Int {
        container.get().readBinary(ba4);
        return ((ba4[0].toInt() and 0xFF) + (ba4[1].toInt() and 0xFF shl 8)
                + (ba4[2].toInt() and 0xFF shl 16) + (ba4[3].toInt() and 0xFF shl 24))
    }


    val ba8 = ByteArray(8)

    suspend fun readLong(): Long {
        container.get().readBinary(ba8);
        return ((ba8[0].toLong() and 0xFF) + (ba8[1].toLong() and 0xFF shl 8)
                + (ba8[2].toLong() and 0xFF shl 16) + (ba8[3].toLong() and 0xFF shl 24)
                + (ba8[4].toLong() and 0xFF shl 32) + (ba8[5].toLong() and 0xFF shl 40)
                + (ba8[6].toLong() and 0xFF shl 48) + (ba8[7].toLong() and 0xFF shl 56))
    }

    suspend fun readLongs(dest: LongArray) {
        val ba = ByteArray(dest.size * 8)
        container.get().readBinary(ba)
        var off = 0;
        for (row in 0 until dest.size) {
            dest[row] = ((ba[off+0].toLong() and 0xFF) + (ba[off+1].toLong() and 0xFF shl 8)
                    + (ba[off+2].toLong() and 0xFF shl 16) + (ba[off+3].toLong() and 0xFF shl 24)
                    + (ba[off+4].toLong() and 0xFF shl 32) + (ba[off+5].toLong() and 0xFF shl 40)
                    + (ba[off+6].toLong() and 0xFF shl 48) + (ba[off+7].toLong() and 0xFF shl 56))
            off += 8
        }
    }


    suspend fun readBoolean(): Boolean {
        return container.get().readBinary() != 0L
    }


    suspend fun readStringBinary(): String {
        val data = ByteArray(readVarInt().toInt())
        return if (container.get().readBinary(data) > 0) String(data) else ""
    }


    suspend fun readByte(): Byte {
        return container.get().readBinary().toByte()
    }

    fun maybeEnableCompressed() {
        container.select(true)
    }

    fun maybeDisenableCompressed() {
        container.select(false)
    }


    suspend fun readFloat(): Float {
        return java.lang.Float.intBitsToFloat(((container.get().readBinary() and 0xFF) + (container.get().readBinary() and 0xFF shl 8)
                + (container.get().readBinary() and 0xFF shl 16) + (container.get().readBinary() shl 24)).toInt())
    }


    suspend fun readDouble(): Double {
        return java.lang.Double.longBitsToDouble(
                ((container.get().readBinary() and 0xFFL)
                        + (container.get().readBinary() and 0xFFL shl 8)
                        + (container.get().readBinary() and 0xFFL shl 16)
                        + (container.get().readBinary() and 0xFFL shl 24)
                        + (container.get().readBinary() and 0xFFL shl 32)
                        + (container.get().readBinary() and 0xFFL shl 40)
                        + (container.get().readBinary() and 0xFFL shl 48)
                        + (container.get().readBinary() and 0xFFL shl 56))
        )
    }


    suspend fun readBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        container.get().readBinary(bytes)
        return bytes
    }

    init {
        val socketReader = SocketBuffedReader(socket)
        container = Container(socketReader, socketReader)
    }
}