package com.github.housepower.jdbc.wrapper

import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLWarning
import java.util.concurrent.Executor

abstract class SQLConnection {

    @get:Throws(SQLException::class)
    @set:Throws(SQLException::class)
    var autoCommit: Boolean
        get() = false
        set(autoCommit) {
        }

    
    fun commit() {
    }

    
    fun rollback() {
    }

    @get:Throws(SQLException::class)
    @set:Throws(SQLException::class)
    var isReadOnly: Boolean
        get() = false
        set(readOnly) {
        }

    @get:Throws(SQLException::class)
    @set:Throws(SQLException::class)
    var transactionIsolation: Int
        get() = Connection.TRANSACTION_NONE
        set(level) {
        }

    @get:Throws(SQLException::class)
    val warnings: SQLWarning?
        get() = null

    
    fun clearWarnings() {
    }

    @get:Throws(SQLException::class)
    val typeMap: Map<String, Class<*>>?
        get() = null

    
    fun setTypeMap(map: Map<String?, Class<*>?>?) {
    }

    @get:Throws(SQLException::class)
    @set:Throws(SQLException::class)
    var holdability: Int
        get() = 0
        set(holdability) {
        }

    
    suspend fun abort(executor: Executor?) {
        close()
    }

    open suspend fun close() {}

    
    fun setNetworkTimeout(executor: Executor?, milliseconds: Int) {
    }

    @get:Throws(SQLException::class)
    val networkTimeout: Int
        get() = 0
}