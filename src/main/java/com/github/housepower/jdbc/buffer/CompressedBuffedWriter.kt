package com.github.housepower.jdbc.buffer

import com.github.housepower.jdbc.misc.ClickHouseCityHash
import net.jpountz.lz4.LZ4Factory
import java.io.IOException
import kotlin.experimental.and

class CompressedBuffedWriter(private val capacity: Int, writer: BuffedWriter) : BuffedWriter {
    private var position = 0
    private val writtenBuf: ByteArray
    private val writer: BuffedWriter
    private val lz4Compressor = LZ4Factory.safeInstance().fastCompressor()


    override suspend fun writeBinary(byt: Byte) {
        writtenBuf[position++] = byt
        flushToTarget(false)
    }

    override suspend fun writeBinary(bytes: ByteArray) {
        writeBinary(bytes, 0, bytes.size.toInt())
    }

    override suspend fun writeBinary(bytes: ByteArray, offset: Int, length: Int) {
        var offset = offset
        var length = length
        while (remaing() < length) {
            val num = remaing()
            System.arraycopy(bytes, offset, writtenBuf, position, remaing())
            position += num
            flushToTarget(false)
            offset += num
            length -= num
        }
        System.arraycopy(bytes, offset, writtenBuf, position, length)
        position += length
        flushToTarget(false)
    }

    override suspend fun flushToTarget(force: Boolean) {
        if (position > 0 && (force || !hasRemaining())) {
            val maxLen = lz4Compressor.maxCompressedLength(position)
            val compressedBuffer = ByteArray(maxLen + 9 + 16)
            val res = lz4Compressor.compress(writtenBuf, 0, position, compressedBuffer, 9 + 16)
            compressedBuffer[16] = (0x82 and 0xFF).toByte()
            val compressedSize = res + COMPRESSION_HEADER_LENGTH
            System.arraycopy(littleEndian(compressedSize), 0, compressedBuffer, 17, 4)
            System.arraycopy(littleEndian(position), 0, compressedBuffer, 21, 4)
            val checksum = ClickHouseCityHash.cityHash128(compressedBuffer, 16, compressedSize)
            System.arraycopy(littleEndian(checksum[0]), 0, compressedBuffer, 0, 8)
            System.arraycopy(littleEndian(checksum[1]), 0, compressedBuffer, 8, 8)
            writer.writeBinary(compressedBuffer, 0, compressedSize + 16)
            position = 0
        }
    }

    private fun hasRemaining(): Boolean {
        return position < capacity
    }

    private fun remaing(): Int {
        return capacity - position
    }

    private fun littleEndian(x: Int): ByteArray {
        val data = ByteArray(4)
        data[0] = (x and 0xFF).toByte()
        data[1] = ((x shr 8).toByte() and 0xFF.toByte())
        data[2] = ((x shr 16).toByte() and 0xFF.toByte())
        data[3] = ((x shr 24).toByte() and 0xFF.toByte())
        return data
    }

    private fun littleEndian(x: Long): ByteArray {
        val data = ByteArray(8)
        data[0] = (x and 0xFF).toByte()
        data[1] = ((x shr 8).toByte() and 0xFF.toByte())
        data[2] = ((x shr 16).toByte() and 0xFF.toByte())
        data[3] = ((x shr 24).toByte() and 0xFF.toByte())
        data[4] = ((x shr 32).toByte() and 0xFF.toByte())
        data[5] = ((x shr 40).toByte() and 0xFF.toByte())
        data[6] = ((x shr 48).toByte() and 0xFF.toByte())
        data[7] = ((x shr 56).toByte() and 0xFF.toByte())
        return data
    }

    companion object {
        private const val COMPRESSION_HEADER_LENGTH = 9
    }

    init {
        writtenBuf = ByteArray(capacity)
        this.writer = writer
    }
}