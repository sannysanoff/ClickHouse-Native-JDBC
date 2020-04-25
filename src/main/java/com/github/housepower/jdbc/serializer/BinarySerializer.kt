package com.github.housepower.jdbc.serializer

import com.github.housepower.jdbc.buffer.BuffedWriter
import com.github.housepower.jdbc.buffer.CompressedBuffedWriter
import com.github.housepower.jdbc.misc.Container
import com.github.housepower.jdbc.settings.ClickHouseDefines
import java.io.IOException
import kotlin.experimental.and
import kotlin.experimental.or

class BinarySerializer(writer: BuffedWriter?, private val enableCompress: Boolean) {
    private val container: Container<BuffedWriter?>

    @Throws(IOException::class)
    suspend fun writeVarInt(x0: Long) {
        var x = x0;
        for (i in 0..8) {
            var byt = (x and 0x7F).toByte()
            if (x > 0x7F) {
                byt = byt or 0x80.toByte()
            }
            x = x shr 7
            container.get()!!.writeBinary(byt.toByte())
            if (x == 0L) {
                return
            }
        }
    }

    @Throws(IOException::class)
    suspend fun writeByte(x: Byte) {
        container.get()!!.writeBinary(x)
    }

    @Throws(IOException::class)
    suspend fun writeBoolean(x: Boolean) {
        writeVarInt((if (x) 1 else 0).toLong())
    }

    @Throws(IOException::class)
    suspend fun writeShort(i: Short) {
        container.get()!!.writeBinary((i and 0xFF).toByte())
        container.get()!!.writeBinary(((i.toInt() shr 8) and 0xFF).toByte())
    }

    @Throws(IOException::class)
    suspend fun writeInt(i: Int) {
        container.get()!!.writeBinary((i and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 8 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 16 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 24 and 0xFF).toByte())
    }

    @Throws(IOException::class)
    suspend fun writeLong(i: Long) {
        container.get()!!.writeBinary((i and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 8 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 16 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 24 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 32 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 40 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 48 and 0xFF).toByte())
        container.get()!!.writeBinary((i shr 56 and 0xFF).toByte())
    }

    @Throws(IOException::class)
    suspend fun writeStringBinary(binary: String) {
        val bs = binary.toByteArray()
        writeVarInt(bs.size.toLong())
        container.get()!!.writeBinary(bs)
    }

    @Throws(IOException::class)
    suspend fun flushToTarget(force: Boolean) {
        container.get()!!.flushToTarget(force)
    }

    fun maybeEnableCompressed() {
        if (enableCompress) container.select(true)
    }

    @Throws(IOException::class)
    suspend fun maybeDisableCompressed() {
        if (enableCompress) {
            container.get()!!.flushToTarget(true)
            container.select(false)
        }
    }

    @Throws(IOException::class)
    suspend fun writeFloat(datum: Float) {
        val x = java.lang.Float.floatToIntBits(datum)
        writeInt(x)
    }

    @Throws(IOException::class)
    suspend fun writeDouble(datum: Double) {
        val x = java.lang.Double.doubleToLongBits(datum)
        container.get()!!.writeBinary((x and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 8 and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 16 and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 24 and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 32 and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 40 and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 48 and 0xFF).toByte())
        container.get()!!.writeBinary((x ushr 56 and 0xFF).toByte())
    }

    @Throws(IOException::class)
    suspend fun writeBytes(bytes: ByteArray?) {
        container.get()!!.writeBinary(bytes!!)
    }

    init {
        var compressBuffer: BuffedWriter? = null
        if (enableCompress) {
            compressBuffer = CompressedBuffedWriter(ClickHouseDefines.SOCKET_BUFFER_SIZE, writer!!)
        }
        container = Container(writer, compressBuffer)
    }
}