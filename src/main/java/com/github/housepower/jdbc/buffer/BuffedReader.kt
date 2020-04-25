package com.github.housepower.jdbc.buffer

import java.io.IOException

interface BuffedReader {
    suspend fun readBinary(): Long

    suspend fun readBinary(bytes: ByteArray): Int
}