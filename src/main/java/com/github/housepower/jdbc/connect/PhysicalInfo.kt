package com.github.housepower.jdbc.connect

import com.github.housepower.jdbc.protocol.QueryRequest.ClientInfo
import com.github.housepower.jdbc.settings.ClickHouseConfig
import java.util.*

class PhysicalInfo(private val client: ClientInfo, private val server: ServerInfo, private val connection: PhysicalConnection) {
    fun client(): ClientInfo {
        return client
    }

    fun server(): ServerInfo {
        return server
    }

    fun connection(): PhysicalConnection {
        return connection
    }

    class ServerInfo(val configure: ClickHouseConfig, private val reversion: Long, private val timeZone: TimeZone, private val displayName: String) {
        fun reversion(): Long {
            return reversion
        }

        fun timeZone(): TimeZone {
            return timeZone
        }

        fun displayName(): String {
            return displayName
        }

    }

}