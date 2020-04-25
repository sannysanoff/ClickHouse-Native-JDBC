package com.github.housepower.jdbc.buffer

import io.ktor.network.sockets.Socket
import java.nio.ByteBuffer


class SocketOutputStream(val sock: Socket) : OutputStream() {
    

    val bb = ByteBuffer.allocate(1)

    override suspend fun write(b: Int) {
        bb.put(0, b.toByte());
        sock.write(bb)
    }



    override suspend fun write(b: ByteArray) {
        sock.write(ByteBuffer.wrap(b))
    }



    override suspend fun write(b: ByteArray, off: Int, len: Int) {
        sock.write(ByteBuffer.wrap(b, off, len))
    }



    override suspend fun flush() {
    }



    override suspend fun close() {
        sock.close()
    }


}
