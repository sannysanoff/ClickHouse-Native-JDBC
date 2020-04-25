package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException

class ProgressResponse(val newRows: Long, val newBytes: Long, val newTotalRows: Long) : RequestOrResponse(ProtocolType.RESPONSE_Progress) {

    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("ProgressResponse Cannot write to Server.")
    }

    companion object {
        @Throws(IOException::class)
        suspend fun readFrom(deserializer: BinaryDeserializer): ProgressResponse {
            return ProgressResponse(deserializer.readVarInt(), deserializer.readVarInt(), deserializer.readVarInt())
        }
    }

}