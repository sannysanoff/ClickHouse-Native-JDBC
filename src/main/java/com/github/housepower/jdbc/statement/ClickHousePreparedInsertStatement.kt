package com.github.housepower.jdbc.statement

import com.github.housepower.jdbc.ClickHouseConnection
import com.github.housepower.jdbc.ClickHouseResultSet
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.Validate.isTrue
import com.github.housepower.jdbc.stream.ValuesWithParametersInputFormat
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class ClickHousePreparedInsertStatement(private val posOfData: Int, private val fullQuery: String,
                                        conn: ClickHouseConnection?) : AbstractPreparedStatement(conn!!, null) {
    private val insertQuery: String
    private var blockInit = false
    private fun initBlockIfPossible() {
        if (blockInit) {
            return
        }
        block = getSampleBlock(insertQuery)
        block!!.initWriteBuffer()
        blockInit = true
        ValuesWithParametersInputFormat(fullQuery, posOfData).fillBlock(block!!)
    }

    fun execute(): Boolean {
        return executeQuery() != null
    }

    override fun executeUpdate(): Int {
        addParameters()
        val result = connection.sendInsertRequest(block!!)
        blockInit = false
        block!!.initWriteBuffer()
        return result
    }

    override fun executeQuery(): ClickHouseResultSet {
        executeUpdate()
        throw SQLException("query on insert stmt")
    }

    fun addBatch() {
        addParameters()
    }

    override fun setObject(index: Int, x: Any?) {
        initBlockIfPossible()
        block!!.setObject(index - 1, x)
    }

    private fun addParameters() {
        block!!.appendRow()
    }

    fun clearBatch() {}
    fun executeBatch(): IntArray {
        val rows = connection.sendInsertRequest(block!!)
        val result = IntArray(rows)
        Arrays.fill(result, -1)
        clearBatch()
        blockInit = false
        block!!.initWriteBuffer()
        return result
    }

    override fun close() {
        if (blockInit) {
            // Empty insert when close.
            connection.sendInsertRequest(Block())
            blockInit = false
            block!!.initWriteBuffer()
        }
        super.close()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(super.toString())
        sb.append(": ")
        try {
            sb.append("$insertQuery (")
            for (i in 0 until block!!.columns()) {
                val obj = block!!.getObject(i)
                if (obj == null) {
                    sb.append("?")
                } else if (obj is Number) {
                    sb.append(obj)
                } else {
                    sb.append("'$obj'")
                }
                if (i < block!!.columns() - 1) {
                    sb.append(",")
                }
            }
            sb.append(")")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    companion object {
        private fun computeQuestionMarkSize(query: String, start: Int): Int {
            var param = 0
            var inQuotes = false
            var inBackQuotes = false
            for (i in 0 until query.length) {
                val ch = query[i]
                if (ch == '`') {
                    inBackQuotes = !inBackQuotes
                } else if (ch == '\'') {
                    inQuotes = !inQuotes
                } else if (!inBackQuotes && !inQuotes) {
                    if (ch == '?') {
                        isTrue(i > start, "")
                        param++
                    }
                }
            }
            return param
        }
    }

    init {
        insertQuery = fullQuery.substring(0, posOfData)
        initBlockIfPossible()
    }
}