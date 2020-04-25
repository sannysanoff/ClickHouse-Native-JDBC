package com.github.housepower.jdbc.buffer

import com.github.housepower.jdbc.settings.ClickHouseDefines
import io.ktor.network.sockets.Socket
import java.io.EOFException
import kotlin.experimental.and

class SocketBuffedReader internal constructor(private val input: SocketInputStream, private val capacity: Int) : BuffedReader {
    private var limit = 0
    private var position = 0
    private val buf: ByteArray

    constructor(socket: Socket?) : this(SocketInputStream(socket!!), ClickHouseDefines.SOCKET_BUFFER_SIZE)


    override suspend fun readBinary(): Long {
        if (!remaining() && !refill()) {
            throw EOFException("Attempt to read after eof.")
        }
        return (buf[position++] and 0xFF.toByte()).toLong()
    }

    
    override suspend fun readBinary(bytes: ByteArray): Int {
        var i = 0
        while (i < bytes.size) {
            if (!remaining() && !refill()) {
                throw EOFException("Attempt to read after eof.")
            }
            val pending = bytes.size - i
            val fillLength = Math.min(pending, limit - position)
            if (fillLength > 0) {
                System.arraycopy(buf, position, bytes, i, fillLength)
                i += fillLength
                position += fillLength
            }
        }
        return bytes.size
    }

    private fun remaining(): Boolean {
        return position < limit
    }

    
    private suspend fun refill(): Boolean {
        if (!remaining() && input.read(buf, 0, capacity).also { limit = it } <= 0) {
            throw EOFException("Attempt to read after eof.")
        }
        position = 0
        return true
    }

    init {
        buf = ByteArray(capacity)
    }
}