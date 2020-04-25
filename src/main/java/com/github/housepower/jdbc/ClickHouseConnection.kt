package com.github.housepower.jdbc

import com.github.housepower.jdbc.connect.PhysicalConnection
import com.github.housepower.jdbc.connect.PhysicalInfo
import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.CheckedSupplier
import com.github.housepower.jdbc.misc.Validate
import com.github.housepower.jdbc.protocol.QueryRequest.ClientInfo
import com.github.housepower.jdbc.protocol.QueryResponse
import com.github.housepower.jdbc.settings.ClickHouseConfig
import com.github.housepower.jdbc.settings.ClickHouseDefines
import com.github.housepower.jdbc.statement.AbstractPreparedStatement
import com.github.housepower.jdbc.statement.ClickHousePreparedInsertStatement
import com.github.housepower.jdbc.statement.ClickHousePreparedQueryStatement
import com.github.housepower.jdbc.statement.ClickHouseStatement
import com.github.housepower.jdbc.wrapper.SQLConnection
import java.net.InetSocketAddress
import java.sql.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import java.util.regex.Pattern

class ClickHouseConnection protected constructor(configure: ClickHouseConfig, info: PhysicalInfo) : SQLConnection() {
    // Just to be variable
    private val isClosed: AtomicBoolean
    private val configure: ClickHouseConfig
    private val atomicInfo: AtomicReference<PhysicalInfo>
    private var state = ConnectionState.IDLE


    override fun close() {
        if (!isClosed() && isClosed.compareAndSet(false, true)) {
            val connection = atomicInfo.get().connection()
            connection.disPhysicalConnection()
        }
    }


    fun isClosed(): Boolean {
        return isClosed.get()
    }

    fun createStatement(): ClickHouseStatement {
        Validate.isTrue(!isClosed(), "Unable to create Statement, because the connection is closed.")
        return ClickHouseStatement(this)
    }

    fun prepareStatement(query: String): AbstractPreparedStatement {
        Validate.isTrue(!isClosed(), "Unable to create PreparedStatement, because the connection is closed.")
        val matcher = VALUES_REGEX.matcher(query)
        return if (matcher.find()) ClickHousePreparedInsertStatement(matcher.end() - 1, query, this) else ClickHousePreparedQueryStatement(this, query)
    }


    fun prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int): AbstractPreparedStatement {
        return this.prepareStatement(sql)
    }


    fun setClientInfo(properties: Properties) {
        configure.parseJDBCProperties(properties)
    }


    fun setClientInfo(name: String, value: String) {
        val properties = Properties()
        properties[name] = value
        configure.parseJDBCProperties(properties)
    }


    fun createArrayOf(typeName: String, elements: Array<Any>): java.sql.Array {
        Validate.isTrue(!isClosed(), "Unable to create Array, because the connection is closed.")
        return ClickHouseArray(elements)
    }


    fun createStruct(typeName: String, attributes: Array<Any>): Struct {
        Validate.isTrue(!isClosed(), "Unable to create Struct, because the connection is closed.")
        return ClickHouseStruct(typeName, attributes)
    }


    fun isValid(timeout: Int): Boolean {
        val validConfigure = configure.copy()
        validConfigure.setQueryTimeout(timeout * 1000)
        var connection: ClickHouseConnection? = null
        var statement: ClickHouseStatement? = null
        return try {
            connection = ClickHouseConnection(validConfigure, atomicInfo.get())
            statement = connection.createStatement()
            statement.execute("SELECT 1")
            statement.close()
            true
        } finally {
            statement?.close()
            connection?.close()
        }
    }


    fun getSampleBlock(insertQuery: String?): Block {
        val connection = healthyPhysicalConnection
        connection.sendQuery(insertQuery, atomicInfo.get().client(), configure.settings())
        state = ConnectionState.WAITING_INSERT
        return connection.receiveSampleBlock(configure.queryTimeout(), atomicInfo.get().server())
    }


    fun sendQueryRequest(query: String?): QueryResponse {
        if (state == ConnectionState.WAITING_INSERT) {
            throw RuntimeException("Connection is currently waiting for an insert operation, check your previous InsertStatement.")
        }
        val connection = healthyPhysicalConnection
        connection.sendQuery(query, atomicInfo.get().client(), configure.settings())
        return QueryResponse(CheckedSupplier { connection.receiveResponse(configure.queryTimeout(), atomicInfo.get().server()) })
    }

    // when sendInsertRequest we must ensure the connection is healthy
    // the sampleblock mus be called before this method

    fun sendInsertRequest(block: Block): Int {
        if (state != ConnectionState.WAITING_INSERT) {
            throw RuntimeException("Call getSampleBlock before insert.")
        }
        val connection = physicalConnection
        connection.sendData(block)
        connection.sendData(Block())
        connection.receiveEndOfStream(configure.queryTimeout(), atomicInfo.get().server())
        state = ConnectionState.IDLE
        return block.rows()
    }

    @get:Throws(SQLException::class)
    private val healthyPhysicalConnection: PhysicalConnection
        private get() {
            val oldInfo = atomicInfo.get()
            if (!oldInfo.connection().ping(configure.queryTimeout(), atomicInfo.get().server())) {
                val newInfo = createPhysicalInfo(configure)
                val closeableInfo = if (atomicInfo.compareAndSet(oldInfo, newInfo)) oldInfo else newInfo
                closeableInfo.connection().disPhysicalConnection()
            }
            return atomicInfo.get().connection()
        }

    @get:Throws(SQLException::class)
    private val physicalConnection: PhysicalConnection
        private get() = atomicInfo.get().connection()

    companion object {
        private val VALUES_REGEX = Pattern.compile("[V|v][A|a][L|l][U|u][E|e][S|s]\\s*\\(")

        @JvmStatic

        fun createClickHouseConnection(configure: ClickHouseConfig): ClickHouseConnection {
            return ClickHouseConnection(configure, createPhysicalInfo(configure))
        }


        private fun createPhysicalInfo(configure: ClickHouseConfig): PhysicalInfo {
            val physical = PhysicalConnection.openPhysicalConnection(configure)
            return PhysicalInfo(clientInfo(physical, configure), serverInfo(physical, configure), physical)
        }


        private fun clientInfo(physical: PhysicalConnection, configure: ClickHouseConfig): ClientInfo {
            Validate.isTrue(physical.address() is InetSocketAddress)
            val address = physical.address() as InetSocketAddress
            val clientName = String.format("%s %s", ClickHouseDefines.NAME, "client")
            val initialAddress = "[::ffff:127.0.0.1]:0"
            return ClientInfo(initialAddress, address.hostName, clientName)
        }


        private fun serverInfo(physical: PhysicalConnection, configure: ClickHouseConfig): ServerInfo {
            return try {
                val reversion = ClickHouseDefines.CLIENT_REVERSION.toLong()
                physical.sendHello("client", reversion, configure.database(), configure.username(), configure.password())
                val response = physical.receiveHello(configure.queryTimeout(), null)
                val timeZone = TimeZone.getTimeZone(response.serverTimeZone())
                ServerInfo(configure, response.reversion(), timeZone, response.serverDisplayName())
            } catch (rethrows: SQLException) {
                physical.disPhysicalConnection()
                throw rethrows
            }
        }
    }

    init {
        isClosed = AtomicBoolean(false)
        this.configure = configure
        atomicInfo = AtomicReference(info)
    }
}