package com.github.housepower.jdbc.statement

import com.github.housepower.jdbc.ClickHouseConnection
import com.github.housepower.jdbc.ClickHouseResultSet
import com.github.housepower.jdbc.ClickHouseResultSetMetaData
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.stream.ValuesInputFormat
import com.github.housepower.jdbc.wrapper.SQLStatement
import java.sql.Connection
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.regex.Pattern

open class ClickHouseStatement(val connection: ClickHouseConnection) : SQLStatement() {
    private var lastResultSet: ClickHouseResultSet? = null
    @JvmField
    protected var block: Block? = null

    fun execute(query: String): Boolean {
        return executeQuery(query) != null
    }

    fun executeQuery(query: String): ClickHouseResultSet {
        executeUpdate(query)
        return lastResultSet!!
    }

    fun executeUpdate(query: String): Int {
        val matcher = VALUES_REGEX.matcher(query)
        if (matcher.find()) {
            lastResultSet = null
            val insertQuery = query.substring(0, matcher.end() - 1)
            block = getSampleBlock(insertQuery)
            block!!.initWriteBuffer()
            ValuesInputFormat(matcher.end() - 1, query).fillBlock(block)
            return connection.sendInsertRequest(block!!)
        }
        val response = connection.sendQueryRequest(query)
        lastResultSet = ClickHouseResultSet(response.header()!!, response.data().get(), this)
        return 0
    }

    open fun close() {
    }

    fun getResultSet(): ClickHouseResultSet {
        return lastResultSet!!
    }

    fun getMetaData(): ClickHouseResultSetMetaData {
        return lastResultSet!!.getMetaData()
    }

    fun getSampleBlock(insertQuery: String?): Block {
        return connection.getSampleBlock(insertQuery)
    }

    companion object {
        private val VALUES_REGEX = Pattern.compile("[V|v][A|a][L|l][U|u][E|e][S|s]\\s*\\(")
    }

}