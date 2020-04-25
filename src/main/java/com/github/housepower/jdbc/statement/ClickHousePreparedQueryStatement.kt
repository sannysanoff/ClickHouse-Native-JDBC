package com.github.housepower.jdbc.statement

import com.github.housepower.jdbc.ClickHouseConnection
import com.github.housepower.jdbc.ClickHouseResultSet
import java.util.*

class ClickHousePreparedQueryStatement private constructor(conn: ClickHouseConnection, parts: Array<String>) : AbstractPreparedStatement(conn, parts) {
    constructor(conn: ClickHouseConnection, query: String) : this(conn, splitQueryByQuestionMark(query)) {}

    
    suspend fun execute(): Boolean {
        return execute(assembleQueryPartsAndParameters())
    }

    override suspend fun executeUpdate(): Int {
        return executeUpdate(assembleQueryPartsAndParameters())
    }

    override suspend fun executeQuery(): ClickHouseResultSet {
        return executeQuery(assembleQueryPartsAndParameters())
    }

    override suspend fun setObject(index: Int, x: Any?) {
        paramz[index - 1] = x
    }

    override fun toString(): String {
        val queryBuilder = StringBuilder()
        queryBuilder.append(super.toString())
        try {
            queryBuilder.append(": ")
            queryBuilder.append(assembleQueryPartsAndParameters())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return queryBuilder.toString()
    }

    companion object {
        private fun splitQueryByQuestionMark(query: String): Array<String> {
            var lastPos = 0
            val queryParts: MutableList<String> = ArrayList()
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
                        queryParts.add(query.substring(lastPos, i))
                        lastPos = i + 1
                    }
                }
            }
            queryParts.add(query.substring(lastPos, query.length))
            return queryParts.toTypedArray()
        }
    }
}