package com.github.housepower.jdbc

import com.github.housepower.jdbc.ClickHouseConnection.Companion.createClickHouseConnection
import com.github.housepower.jdbc.settings.ClickHouseConfig
import com.github.housepower.jdbc.settings.ClickHouseDefines
import java.sql.DriverPropertyInfo
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.util.*

open class NonRegisterDriver {
    fun acceptsURL(url: String): Boolean {
        return url.startsWith(CLICK_HOUSE_JDBC_PREFIX)
    }

    suspend fun connect(url: String, properties: Properties?): ClickHouseConnection? {
        if (!acceptsURL(url)) {
            println("Not accepting url: $url")
            return null
        }
        val configure = ClickHouseConfig(url, properties!!)
        return createClickHouseConnection(configure)
    }

    @Throws(SQLException::class)
    fun getPropertyInfo(url: String?, properties: Properties?): Array<DriverPropertyInfo?> {
        val configure = ClickHouseConfig(url!!, properties!!)
        var index = 0
        val driverPropertiesInfo = arrayOfNulls<DriverPropertyInfo>(configure.settings()!!.size)
        for ((key, value1) in configure.settings()!!) {
            val value = value1.toString()
            val property = DriverPropertyInfo(key.name, value)
            property.description = key.describe()
            driverPropertiesInfo[index++] = property
        }
        return driverPropertiesInfo
    }

    val majorVersion: Int
        get() = ClickHouseDefines.MAJOR_VERSION

    val minorVersion: Int
        get() = ClickHouseDefines.MINOR_VERSION

    fun jdbcCompliant(): Boolean {
        return false
    }

    companion object {
        private const val JDBC_PREFIX = "jdbc:"
        private const val CLICK_HOUSE_JDBC_PREFIX = JDBC_PREFIX + "clickhouse:"
    }
}