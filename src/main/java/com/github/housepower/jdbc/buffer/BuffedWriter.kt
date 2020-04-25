package com.github.housepower.jdbc.buffer

import java.io.IOException

interface BuffedWriter {
    suspend fun writeBinary(byt: Byte)

    suspend fun writeBinary(bytes: ByteArray)

    suspend fun writeBinary(bytes: ByteArray, offset: Int, length: Int)

    suspend fun flushToTarget(force: Boolean)
}