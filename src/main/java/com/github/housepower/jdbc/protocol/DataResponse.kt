package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class DataResponse(private val name: String, private val block: Block) : RequestOrResponse(ProtocolType.RESPONSE_Data) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("DataResponse Cannot write to Server.")
    }

    fun block(): Block {
        return block
    }

    companion object {
        @Throws(IOException::class, SQLException::class)
        suspend fun readFrom(deserializer: BinaryDeserializer, info: ServerInfo?): DataResponse {
            val name = deserializer.readStringBinary()
            deserializer.maybeEnableCompressed()
            val block = Block.readFrom(deserializer, info)
            deserializer.maybeDisenableCompressed()
            return DataResponse(name, block)
        }
    }

}