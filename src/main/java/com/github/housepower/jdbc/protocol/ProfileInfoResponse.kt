package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class ProfileInfoResponse internal constructor() : RequestOrResponse(ProtocolType.RESPONSE_ProfileInfo) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("ProfileInfoResponse Cannot write to Server.")
    }

    companion object {
        suspend fun readFrom(deserializer: BinaryDeserializer): ProfileInfoResponse {
            val rows = deserializer.readVarInt()
            val blocks = deserializer.readVarInt()
            val bytes = deserializer.readVarInt()
            val applied_limit = deserializer.readVarInt()
            val rows_before_limit = deserializer.readVarInt()
            val calculated_rows_before_limit = deserializer.readBoolean()
            return ProfileInfoResponse()
        }
    }
}