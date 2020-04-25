package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import com.github.housepower.jdbc.settings.ClickHouseDefines
import java.io.IOException
import java.util.*

class HelloResponse(
        private val serverName: String, private val majorVersion: Long, private val minorVersion: Long, private val reversion: Long, private val serverTimeZone: String,
        private val serverDisplayName: String) : RequestOrResponse(ProtocolType.RESPONSE_HELLO) {
    fun reversion(): Long {
        return reversion
    }

    fun majorVersion(): Long {
        return majorVersion
    }

    fun minorVersion(): Long {
        return minorVersion
    }

    fun serverName(): String {
        return serverName
    }

    fun serverTimeZone(): String {
        return serverTimeZone
    }

    fun serverDisplayName(): String {
        return serverDisplayName
    }

    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("HelloResponse Cannot write to Server.")
    }

    companion object {
        @Throws(IOException::class)
        suspend fun readFrom(deserializer: BinaryDeserializer): HelloResponse {
            val name = deserializer.readStringBinary()
            val majorVersion = deserializer.readVarInt()
            val minorVersion = deserializer.readVarInt()
            val serverReversion = deserializer.readVarInt()
            val serverTimeZone = getTimeZone(deserializer, serverReversion)
            val serverDisplayName = getDisplayName(deserializer, serverReversion)
            return HelloResponse(name, majorVersion, minorVersion, serverReversion, serverTimeZone, serverDisplayName)
        }

        @Throws(IOException::class)
        private suspend fun getTimeZone(deserializer: BinaryDeserializer, serverReversion: Long): String {
            return if (serverReversion >= ClickHouseDefines.DBMS_MIN_REVISION_WITH_SERVER_TIMEZONE) deserializer.readStringBinary() else TimeZone.getDefault().id
        }

        @Throws(IOException::class)
        private suspend fun getDisplayName(deserializer: BinaryDeserializer, serverReversion: Long): String {
            return if (serverReversion >= ClickHouseDefines.DBMS_MIN_REVISION_WITH_SERVER_DISPLAY_NAME) deserializer.readStringBinary() else "localhost"
        }
    }

}