package com.github.housepower.jdbc.wrapper

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException

abstract class SQLResultSetMetaData : ResultSetMetaData {
    
    override fun getColumnCount(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isAutoIncrement(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isCaseSensitive(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isSearchable(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isCurrency(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isNullable(column: Int): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isSigned(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnDisplaySize(column: Int): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnLabel(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnName(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSchemaName(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getPrecision(column: Int): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getScale(column: Int): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getTableName(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getCatalogName(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnType(column: Int): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnTypeName(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isReadOnly(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isWritable(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isDefinitelyWritable(column: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnClassName(column: Int): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun <T> unwrap(iface: Class<T>): T {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        throw SQLFeatureNotSupportedException()
    }
}