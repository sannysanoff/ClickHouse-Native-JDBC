package com.github.housepower.jdbc.buffer

 
class BufferedOutputStream constructor(out: OutputStream, size: Int = 8192) : FilterOutputStream(out) {
    
    protected var buf: ByteArray
    protected var count = 0

    private suspend fun flushBuffer() {
        if (count > 0) {
            out.write(buf, 0, count)
            count = 0
        }
    }

    override suspend fun write(b: Int) {
        if (count >= buf.size) {
            flushBuffer()
        }
        buf[count++] = b.toByte()
    }

    override suspend fun write(b: ByteArray, off: Int, len: Int) {
        if (len >= buf.size) {
            
            flushBuffer()
            out.write(b, off, len)
            return
        }
        if (len > buf.size - count) {
            flushBuffer()
        }
        System.arraycopy(b, off, buf, count, len)
        count += len
    }

    
    override suspend fun flush() {
        flushBuffer()
        out.flush()
    }
    
    
    init {
        require(size > 0) { "Buffer size <= 0" }
        buf = ByteArray(size)
    }
}