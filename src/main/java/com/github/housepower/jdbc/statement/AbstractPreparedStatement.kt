package com.github.housepower.jdbc.statement

import com.github.housepower.jdbc.ClickHouseConnection
import com.github.housepower.jdbc.ClickHouseResultSet
import com.github.housepower.jdbc.misc.Validate
import java.math.BigDecimal
import java.sql.Date
import java.sql.SQLException
import java.sql.Struct
import java.sql.Timestamp
import java.text.SimpleDateFormat

abstract class AbstractPreparedStatement(connection: ClickHouseConnection, private val queryParts: Array<String>?) : ClickHouseStatement(connection!!) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val timestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    protected var paramz: Array<Any?> = arrayOfNulls(0)

    suspend fun setInt(index: Int, x: Int) {
        setObject(index, x)
    }

    suspend fun setByte(index: Int, x: Byte) {
        setObject(index, x)
    }

    suspend fun setLong(index: Int, x: Long) {
        setObject(index, x)
    }

    suspend fun setDate(index: Int, x: Date) {
        setObject(index, x)
    }

    suspend fun setShort(index: Int, x: Short) {
        setObject(index, x)
    }

    suspend fun setFloat(index: Int, x: Float) {
        setObject(index, x)
    }

    suspend fun setArray(index: Int, x: java.sql.Array) {
        setObject(index, x)
    }

    suspend fun setNull(index: Int, type: Int) {
        setObject(index, null)
    }

    suspend fun setDouble(index: Int, x: Double) {
        setObject(index, x)
    }

    suspend fun setString(index: Int, x: String) {
        setObject(index, x)
    }

    suspend fun setTimestamp(index: Int, x: Timestamp) {
        setObject(index, x)
    }

    suspend fun setBigDecimal(index: Int, x: BigDecimal) {
        setObject(index, x)
    }

    fun clearParameters() {
        for (i in paramz.indices) {
            paramz[i] = null
        }
    }

    protected fun assembleQueryPartsAndParameters(): String {
        //TODO: move to DataType
        val queryBuilder = StringBuilder()
        for (i in queryParts!!.indices) {
            if (i - 1 >= 0 && i - 1 < paramz.size) {
                Validate.isTrue(assembleParameter(paramz[i - 1], queryBuilder),
                        "UNKNOWN DataType :" + if (paramz[i - 1] == null) null else paramz[i - 1]!!.javaClass)
            }
            queryBuilder.append(queryParts[i])
        }
        return queryBuilder.toString()
    }

    fun assembleParameter(parameter: Any?, queryBuilder: StringBuilder): Boolean {
        return assembleSimpleParameter(queryBuilder, parameter) || assembleComplexQuotedParameter(queryBuilder, parameter)
    }

    fun assembleSimpleParameter(queryBuilder: StringBuilder, parameter: Any?): Boolean {
        if (parameter == null) {
            return assembleWithoutQuotedParameter(queryBuilder, "Null")
        } else if (parameter is Number) {
            return assembleWithoutQuotedParameter(queryBuilder, parameter)
        } else if (parameter is String) {
            return assembleQuotedParameter(queryBuilder, parameter.toString())
        } else if (parameter is Date || parameter is Timestamp) {
            val format = if (parameter is Date) dateFormat else timestampFormat
            return assembleQuotedParameter(queryBuilder, format.format(parameter))
        }
        return false
    }

    private fun assembleQuotedParameter(queryBuilder: StringBuilder, parameter: String): Boolean {
        queryBuilder.append("'")
        queryBuilder.append(parameter)
        queryBuilder.append("'")
        return true
    }

    private fun assembleWithoutQuotedParameter(queryBuilder: StringBuilder, parameter: Any): Boolean {
        queryBuilder.append(parameter)
        return true
    }

    private fun assembleComplexQuotedParameter(queryBuilder: StringBuilder, parameter: Any?): Boolean {
        if (parameter is java.sql.Array) {
            queryBuilder.append("[")
            val arrayData = parameter.array as Array<Any>
            for (arrayIndex in arrayData.indices) {
                assembleParameter(arrayData[arrayIndex], queryBuilder)
                queryBuilder.append(if (arrayIndex == arrayData.size - 1) "]" else ",")
            }
            return true
        } else if (parameter is Struct) {
            queryBuilder.append("(")
            val structData = parameter.attributes
            for (structIndex in structData.indices) {
                assembleParameter(structData[structIndex], queryBuilder)
                queryBuilder.append(if (structIndex == structData.size - 1) ")" else ",")
            }
            return true
        }
        return false
    }

    abstract suspend fun executeUpdate(): Int;
    abstract suspend fun executeQuery(): ClickHouseResultSet;

    suspend fun <R> use(block: suspend (AbstractPreparedStatement) -> R): R {
        try {
            val rv = block(this)
            return rv;
        } finally {
            this.close();
        }
    }

    open suspend fun addBatch() : Unit = throw NotImplementedError()
    open suspend fun executeBatch(): IntArray = throw NotImplementedError()


    abstract suspend fun setObject(index: Int, x: Any?);

    init {
        if (queryParts != null && queryParts.size > 0) paramz = arrayOfNulls(queryParts.size)
    }
}