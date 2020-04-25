package com.github.housepower.jdbc.settings

import com.github.housepower.jdbc.misc.Validate.isTrue
import java.net.URI
import java.net.URISyntaxException
import java.sql.SQLException
import java.util.*
import java.util.regex.Pattern

class ClickHouseConfig {
    private var port = 0
    private var address: String? = null
    private var database: String? = null
    private var username: String? = null
    private var password: String? = null
    private var soTimeout = 0
    private var connectTimeout = 0
    private var settings: MutableMap<SettingKey, Any>? = null

    private constructor() {}
    constructor(url: String, properties: Properties) {
        settings = parseJDBCUrl(url)
        settings!!.putAll(parseJDBCProperties(properties))
        var obj: Any? = null
        port = (if (settings!!.remove(SettingKey.port).also { obj = it } == null) 9000 else if (obj as Int == -1) 9000 else obj as Int)
        address = if (settings!!.remove(SettingKey.address).also { obj = it } == null) "127.0.0.1" else obj.toString()
        password = if (settings!!.remove(SettingKey.password).also { obj = it } == null) "" else obj.toString()
        username = if (settings!!.remove(SettingKey.user).also { obj = it } == null) "default" else obj.toString()
        database = if (settings!!.remove(SettingKey.database).also { obj = it } == null) "default" else obj.toString()
        soTimeout = if (settings!!.remove(SettingKey.query_timeout).also { obj = it } == null) 0 else obj as Int * 1000
        connectTimeout = (if (settings!!.remove(SettingKey.connect_timeout).also { obj = it } == null) 0 else obj as Int)
    }

    fun port(): Int {
        return port
    }

    fun address(): String? {
        return address
    }

    fun database(): String? {
        return database
    }

    fun username(): String? {
        return username
    }

    fun password(): String? {
        return password
    }

    fun queryTimeout(): Int {
        return soTimeout
    }

    fun connectTimeout(): Int {
        return connectTimeout
    }

    fun settings(): Map<SettingKey, Any>? {
        return settings
    }

    fun parseJDBCProperties(properties: Properties): Map<SettingKey, Any> {
        val settings: MutableMap<SettingKey, Any> = HashMap()
        for ((key, value) in properties) {
            for (settingKey in SettingKey.values()) {
                val name = key.toString()
                if (settingKey.name.equals(name, ignoreCase = true)) {
                    settings[settingKey] = settingKey.type().deserializeURL(value.toString())
                }
            }
        }
        return settings
    }

    private fun parseJDBCUrl(jdbcUrl: String): MutableMap<SettingKey, Any> {
        return try {
            val uri = URI(jdbcUrl.substring(5))
            val settings: MutableMap<SettingKey, Any> = HashMap()
            val database = uri.path
            if (database != null && !database.isEmpty()) {
                val m = DB_PATH_PATTERN.matcher(database)
                if (m.matches()) {
                    settings[SettingKey.database] = m.group(1)
                } else {
                    throw URISyntaxException("wrong database name path: '$database'", jdbcUrl)
                }
            }
            settings[SettingKey.port] = uri.port
            settings[SettingKey.address] = uri.host
            settings.putAll(extractQueryParameters(uri.query))
            settings
        } catch (ex: URISyntaxException) {
            throw SQLException(ex.message, ex)
        }
    }

    private fun extractQueryParameters(queryParameters: String?): Map<SettingKey, Any> {
        val parameters: MutableMap<SettingKey, Any> = HashMap()
        val tokenizer = StringTokenizer(queryParameters ?: "", "&")
        while (tokenizer.hasMoreTokens()) {
            val queryParameter = tokenizer.nextToken().split("=".toRegex(), 2).toTypedArray()
            isTrue(queryParameter.size == 2,
                    "ClickHouse JDBC URL Parameter '$queryParameters' Error, Expected '='.")
            for (settingKey in SettingKey.values()) {
                if (settingKey.name.equals(queryParameter[0], ignoreCase = true)) {
                    parameters[settingKey] = settingKey.type().deserializeURL(queryParameter[1])
                }
            }
        }
        return parameters
    }

    fun setQueryTimeout(timeout: Int) {
        soTimeout = timeout
    }

    fun copy(): ClickHouseConfig {
        val configure = ClickHouseConfig()
        configure.port = port
        configure.address = address
        configure.database = database
        configure.username = username
        configure.password = password
        configure.soTimeout = soTimeout
        configure.connectTimeout = connectTimeout
        configure.settings = HashMap(settings)
        return configure
    }

    companion object {
        val DB_PATH_PATTERN = Pattern.compile("/([a-zA-Z0-9_]+)")
    }
}