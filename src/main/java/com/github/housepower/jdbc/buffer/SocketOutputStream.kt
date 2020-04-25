package com.github.housepower.jdbc.buffer

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openWriteChannel
import java.nio.ByteBuffer


class SocketOutputStream(val sock: Socket) : OutputStream() {

    val writec = sock.openWriteChannel()

    val bb = ByteBuffer.allocate(1)

    override suspend fun write(b: Int) {
        writec.writeByte(b.toByte())
    }

    override suspend fun write(b: ByteArray) {
        writec.writeFully(b, 0, b.size);
    }


    override suspend fun write(b: ByteArray, off: Int, len: Int) {
        writec.writeFully(b, off, len)
    }

    override suspend fun flush() {
        writec.flush();
    }

    override suspend fun close() {
        writec.close(null)
    }


}
