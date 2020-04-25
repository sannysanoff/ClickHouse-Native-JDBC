package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class PongResponse internal constructor() : RequestOrResponse(ProtocolType.RESPONSE_Pong) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("PongResponse Cannot write to Server.")
    }

    companion object {
        @Throws(IOException::class, SQLException::class)
        fun readFrom(deserializer: BinaryDeserializer?): PongResponse {
            return PongResponse()
        }
    }
}