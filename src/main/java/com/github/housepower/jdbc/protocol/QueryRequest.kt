package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.protocol.DataRequest
import com.github.housepower.jdbc.serializer.BinarySerializer
import com.github.housepower.jdbc.settings.ClickHouseDefines
import com.github.housepower.jdbc.settings.SettingKey
import java.io.IOException
import java.sql.SQLException
import java.util.*

class QueryRequest @JvmOverloads constructor(private val queryId: String, private val clientInfo: ClientInfo, private val stage: Int, private val compression: Boolean, private val queryString: String,
                                             private val settings: Map<SettingKey, Any?> = HashMap()) : RequestOrResponse(ProtocolType.REQUEST_QUERY) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        serializer!!.writeStringBinary(queryId)
        clientInfo.writeTo(serializer)
        for ((key, value) in settings) {
            serializer.writeStringBinary(key.name)
            key.type().serializeSetting(serializer, value!!)
        }
        serializer.writeStringBinary("")
        serializer.writeVarInt(stage.toLong())
        serializer.writeBoolean(compression)
        serializer.writeStringBinary(queryString)
        // empty data to server
        DataRequest.EMPTY.writeTo(serializer)
    }

    class ClientInfo(private val initialAddress: String, private val clientHostname: String, private val clientName: String) {
        suspend fun writeTo(serializer: BinarySerializer?) {
            serializer!!.writeVarInt(INITIAL_QUERY.toLong())
            serializer.writeStringBinary("")
            serializer.writeStringBinary("")
            serializer.writeStringBinary(initialAddress)

            // for TCP kind
            serializer.writeVarInt(TCP_KINE.toLong())
            serializer.writeStringBinary("")
            serializer.writeStringBinary(clientHostname)
            serializer.writeStringBinary(clientName)
            serializer.writeVarInt(ClickHouseDefines.MAJOR_VERSION.toLong())
            serializer.writeVarInt(ClickHouseDefines.MINOR_VERSION.toLong())
            serializer.writeVarInt(ClickHouseDefines.CLIENT_REVERSION.toLong())
            serializer.writeStringBinary("")
        }

        companion object {
            const val TCP_KINE = 1
            const val NO_QUERY: Byte = 0
            const val INITIAL_QUERY: Byte = 1
            const val SECONDARY_QUERY: Byte = 2
        }

    }

    companion object {
        const val COMPLETE_STAGE = 2
    }

}