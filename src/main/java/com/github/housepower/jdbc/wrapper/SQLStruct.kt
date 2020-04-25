package com.github.housepower.jdbc.wrapper

import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.sql.Struct

open class SQLStruct : Struct {
    @Throws(SQLException::class)
    override fun getSQLTypeName(): String {
        throw SQLFeatureNotSupportedException()
    }

    @Throws(SQLException::class)
    override fun getAttributes(): Array<Any> {
        throw SQLFeatureNotSupportedException()
    }

    @Throws(SQLException::class)
    override fun getAttributes(map: Map<String, Class<*>>): Array<Any?> {
        throw SQLFeatureNotSupportedException()
    }
}