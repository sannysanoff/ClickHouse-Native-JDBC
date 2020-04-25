package com.github.housepower.jdbc.buffer

import io.ktor.network.sockets.Socket


class SocketBuffedWriter(socket: Socket) : BuffedWriter {
    private val out: OutputStream = BufferedOutputStream(SocketOutputStream(socket), 8192)

    
    suspend override fun writeBinary(byt: Byte) {
        out.write(byt.toInt())
    }


    suspend override fun writeBinary(bytes: ByteArray) {
        out.write(bytes)
    }


    suspend override fun writeBinary(bytes: ByteArray, offset: Int, length: Int) {
        out.write(bytes, offset, length)
    }


    suspend override fun flushToTarget(force: Boolean) {
        out.flush()
    }

}