package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class ExtremesResponse internal constructor(private val name: String, private val block: Block) : RequestOrResponse(ProtocolType.RESPONSE_Extremes) {

    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("ExtremesResponse Cannot write to Server.")
    }

    companion object {
        suspend fun readFrom(deserializer: BinaryDeserializer, info: ServerInfo?): ExtremesResponse {
            return ExtremesResponse(deserializer.readStringBinary(), Block.readFrom(deserializer, info))
        }
    }

}