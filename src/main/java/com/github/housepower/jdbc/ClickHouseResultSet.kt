package com.github.housepower.jdbc

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.CheckedIterator
import com.github.housepower.jdbc.misc.Validate
import com.github.housepower.jdbc.protocol.DataResponse
import com.github.housepower.jdbc.statement.ClickHouseStatement
import com.github.housepower.jdbc.wrapper.SQLResultSet
import java.math.BigDecimal
import java.net.MalformedURLException
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

class ClickHouseResultSet(private val header: Block, private val iterator: CheckedIterator<DataResponse, SQLException>, val stmt: ClickHouseStatement) : SQLResultSet() {
    var rowIX = -1
    private var current = Block()
    private var lastFetchRow = -1
    private var lastFetchColumn = -1
    private var lastFetchBlock: Block? = null


    inline fun <R> use(block: (ClickHouseResultSet) -> R): R {
        try {
            val rv = block(this)
            return rv;
        } finally {
            this.close();
        }
    }


    /*===================================================================*/

    override fun getInt(index: Int): Int {
        Validate.isTrue(row >= 0 && row < current.rows(),
                "No row information was obtained.You must call ResultSet.next() before that.")
        val column = current.also { lastFetchBlock = it }.getByPosition(index - 1.also { lastFetchColumn = it })
        return column.ints(row.also { lastFetchRow = it })
    }


    override fun getURL(index: Int): URL {
        return try {
            URL(this.getString(index))
        } catch (ex: MalformedURLException) {
            throw SQLException(ex.message, ex)
        }
    }


    override fun getByte(index: Int): Byte {
        val data = getObject(index)
        return (data as Byte).toByte()
    }


    override fun getDate(index: Int): Date {
        val data = getObject(index)
        return data as Date
    }


    override fun getLong(index: Int): Long {
        Validate.isTrue(row >= 0 && row < current.rows(),
                "No row information was obtained.You must call ResultSet.next() before that.")
        val column = current.also { lastFetchBlock = it }.getByPosition(index - 1.also { lastFetchColumn = it })
        return column.longs(row.also { lastFetchRow = it })
    }


    override fun getArray(index: Int): Array {
        val data = getObject(index)
        return data as Array
    }


    override fun getFloat(index: Int): Float {
        val data = getObject(index)
        return (data as Float).toFloat()
    }


    override fun getShort(index: Int): Short {
        val data = getObject(index)
        return (data as Short).toShort()
    }


    override fun getDouble(index: Int): Double {
        Validate.isTrue(row >= 0 && row < current.rows(),
                "No row information was obtained.You must call ResultSet.next() before that.")
        val column = current.also { lastFetchBlock = it }.getByPosition(index - 1.also { lastFetchColumn = it })
        return column.doubles(row.also { lastFetchRow = it })
    }


    override fun getString(index: Int): String {
        val data = getObject(index)
        return data as String
    }


    override fun getObject(index: Int): Any {
        Validate.isTrue(row >= 0 && row < current.rows(),
                "No row information was obtained.You must call ResultSet.next() before that.")
        val column = current.also { lastFetchBlock = it }.getByPosition(index - 1.also { lastFetchColumn = it })
        val rowData = column.values(row.also { lastFetchRow = it })
        return rowData ?: column.type().defaultValue()
    }


    override fun getTimestamp(index: Int): Timestamp {
        val data = getObject(index)
        return data as Timestamp
    }


    override fun getBigDecimal(index: Int): BigDecimal {
        val data = getObject(index)
        return BigDecimal(data.toString())
    }

    /*==================================================================*/

    override fun close() {
        // nothing
    }


    override fun wasNull(): Boolean {
        Validate.isTrue(lastFetchBlock != null, "Please call Result.next()")
        Validate.isTrue(lastFetchColumn >= 0, "Please call Result.getXXX()")
        Validate.isTrue(lastFetchRow >= 0 && lastFetchRow < lastFetchBlock!!.rows(), "Please call Result.next()")
        return lastFetchBlock!!.getByPosition(lastFetchColumn).values(lastFetchRow) == null
    }


//    fun isClosed(): Boolean {
//        return false
//    }
//
    override fun getMetaData(): ClickHouseResultSetMetaData {
        return ClickHouseResultSetMetaData(header)
    }


    override suspend fun next(): Boolean {
        return ++rowIX < current.rows() || 0.also { rowIX = it } < fetchBlock().also { current = it }.rows()
    }


    suspend fun fetchBlock(): Block {
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.block().rows() > 0) {
                return next.block()
            }
        }
        return Block()
    }

}