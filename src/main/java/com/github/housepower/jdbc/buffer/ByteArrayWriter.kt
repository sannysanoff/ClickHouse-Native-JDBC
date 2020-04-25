package com.github.housepower.jdbc.buffer

import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

/**
 *
 */
class ByteArrayWriter(private val blockSize: Int) : BuffedWriter {
    private var buffer: ByteBuffer

    //TODO pooling
    private val byteBufferList: MutableList<ByteBuffer> = LinkedList()

    override suspend fun writeBinary(byt: Byte) {
        buffer.put(byt)
        flushToTarget(false)
    }

    override suspend fun writeBinary(bytes: ByteArray) {
        writeBinary(bytes, 0, bytes.size)
    }
    override suspend fun writeBinary(bytes: ByteArray, offset: Int, length: Int) {
        var offset = offset
        var length = length
        while (buffer.remaining() < length) {
            val num = buffer.remaining()
            buffer.put(bytes, offset, num)
            flushToTarget(true)
            offset += num
            length -= num
        }
        buffer.put(bytes, offset, length)
        flushToTarget(false)
    }

    override suspend fun flushToTarget(force: Boolean) {
        if (buffer.hasRemaining() && !force) {
            return
        }
        buffer = ByteBuffer.allocate(blockSize)
        byteBufferList.add(buffer)
    }

    private fun expend() {
        if (buffer.hasRemaining()) {
            return
        }
        var newCapacity = (buffer.capacity() * expandFactor).toInt()
        while (newCapacity < buffer.capacity() + 1) {
            newCapacity *= expandFactor.toInt()
        }
        val expanded = ByteBuffer.allocate(newCapacity)
        expanded.order(buffer.order())
        buffer.flip()
        expanded.put(buffer)
        buffer = expanded
        byteBufferList[0] = buffer
    }

    val bufferList: List<ByteBuffer>
        get() = byteBufferList

    companion object {
        private const val expandFactor = 1.5f
    }

    init {
        buffer = ByteBuffer.allocate(blockSize)
        byteBufferList.add(buffer)
    }
}