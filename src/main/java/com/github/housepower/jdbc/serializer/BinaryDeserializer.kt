package com.github.housepower.jdbc.serializer

import com.github.housepower.jdbc.buffer.BuffedReader
import com.github.housepower.jdbc.buffer.SocketBuffedReader
import com.github.housepower.jdbc.misc.Container
import io.ktor.network.sockets.Socket

class BinaryDeserializer(socket: Socket?) {
    private val container: Container<BuffedReader>

    
    suspend fun readVarInt(): Long {
        var number = 0L
        for (i in 0..8) {
            val byt = container.get().readBinary()
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

    
    suspend fun readInt(): Int {
        return ((container.get().readBinary() and 0xFF) + ((container.get().readBinary() and 0xFF) shl 8)
                + ((container.get().readBinary() and 0xFF) shl 16) + ((container.get().readBinary() and 0xFF) shl 24)).toInt()
    }

    
    suspend fun readLong(): Long {
        return ((container.get().readBinary() and 0xFFL) + (container.get().readBinary() and 0xFFL shl 8)
                + (container.get().readBinary() and 0xFFL shl 16) + (container.get().readBinary() and 0xFFL shl 24)
                + (container.get().readBinary() and 0xFFL shl 32) + (container.get().readBinary() and 0xFFL shl 40)
                + (container.get().readBinary() and 0xFFL shl 48) + (container.get().readBinary() and 0xFFL shl 56))
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