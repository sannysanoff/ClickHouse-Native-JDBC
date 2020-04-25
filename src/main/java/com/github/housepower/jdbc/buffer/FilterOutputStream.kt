package com.github.housepower.jdbc.buffer


open class FilterOutputStream(var out: OutputStream) : OutputStream() {


    @Volatile
    private var closed = false


    private val closeLock = Any()


    override suspend fun write(b: Int) {
        out.write(b)
    }


    override suspend fun write(b: ByteArray) {
        write(b, 0, b.size)
    }


    override suspend fun write(b: ByteArray, off: Int, len: Int) {
        if (off or len or b.size - (len + off) or off + len < 0) throw IndexOutOfBoundsException()
        for (i in 0 until len) {
            write(b[off + i].toInt())
        }
    }


    override suspend fun flush() {
        out.flush()
    }


    override suspend fun close() {
        if (closed) {
            return
        }
        synchronized(closeLock) {
            if (closed) {
                return
            }
            closed = true
        }
        var flushException: Throwable? = null
        try {
            flush()
        } catch (e: Throwable) {
            flushException = e
            throw e
        } finally {
            if (flushException == null) {
                out.close()
            } else {
                try {
                    out.close()
                } catch (closeException: Throwable) {

                    if (flushException is ThreadDeath &&
                            closeException !is ThreadDeath) {
                        flushException.addSuppressed(closeException)
                        throw (flushException as ThreadDeath?)!!
                    }
                    if (flushException !== closeException) {
                        closeException.addSuppressed(flushException)
                    }
                    throw closeException
                }
            }
        }
    }

}