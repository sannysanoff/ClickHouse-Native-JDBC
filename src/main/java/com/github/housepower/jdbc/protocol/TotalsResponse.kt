package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class TotalsResponse internal constructor(private val name: String, private val block: Block) : RequestOrResponse(ProtocolType.RESPONSE_Totals) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("TotalsResponse Cannot write to Server.")
    }

    companion object {
        @Throws(IOException::class, SQLException::class)
        suspend fun readFrom(deserializer: BinaryDeserializer, info: ServerInfo?): TotalsResponse {
            return TotalsResponse(deserializer.readStringBinary(), Block.readFrom(deserializer, info))
        }
    }

}