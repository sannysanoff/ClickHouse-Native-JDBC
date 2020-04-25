package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinarySerializer
import com.github.housepower.jdbc.settings.ClickHouseDefines
import java.io.IOException

class HelloRequest(private val clientName: String, private val clientReversion: Long, private val defaultDatabase: String, private val clientUsername: String,
                   private val clientPassword: String) : RequestOrResponse(ProtocolType.REQUEST_HELLO) {

    override suspend fun writeImpl(serializer: BinarySerializer) {
        serializer.writeStringBinary(ClickHouseDefines.NAME + " " + clientName)
        serializer.writeVarInt(ClickHouseDefines.MAJOR_VERSION.toLong())
        serializer.writeVarInt(ClickHouseDefines.MINOR_VERSION.toLong())
        serializer.writeVarInt(clientReversion)
        serializer.writeStringBinary(defaultDatabase)
        serializer.writeStringBinary(clientUsername)
        serializer.writeStringBinary(clientPassword)
    }

}