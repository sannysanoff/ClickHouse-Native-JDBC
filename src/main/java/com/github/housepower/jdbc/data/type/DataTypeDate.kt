package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.misc.Validate.isTrue
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import com.github.housepower.jdbc.settings.SettingKey
import org.joda.time.DateTimeZone
import java.io.IOException
import java.sql.Date
import java.sql.SQLException
import java.sql.Types
import java.text.ParseException
import java.text.SimpleDateFormat

class DataTypeDate(serverInfo: ServerInfo) : IDataType {
    private var dateTimeZone: DateTimeZone? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun name(): String {
        return "Date"
    }

    override fun sqlTypeId(): Int {
        return Types.DATE
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Date::class.java
    }

    override fun nullable(): Boolean {
        return false
    }


    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        val mills = (data as Date).time
        val daysSinceEpoch = mills / 1000 / 3600 / 24
        serializer.writeShort(daysSinceEpoch.toShort())
    }


    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        val daysSinceEpoch = deserializer.readShort()
        return Date(3600L * 24 * 1000 * daysSinceEpoch)
    }


    override suspend  fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = arrayOfNulls<Date>(rows)
        for (row in 0 until rows) {
            val daysSinceEpoch = deserializer.readShort()
            data[row] = Date(3600L * 24 * 1000 * daysSinceEpoch)
        }
        return data
    }

    
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        isTrue(lexer.character() == '\'')
        val year: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == '-')
        val month: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == '-')
        val day: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == '\'')
        val timeStr = String.format("%04d-%02d-%02d", year, month, day)
        return try {
            val date = dateFormat.parse(timeStr)
            Date(date.time)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val DEFAULT_VALUE = Date(0)
        @JvmStatic
        fun createDateType(lexer: SQLLexer?, serverInfo: ServerInfo): IDataType {
            return DataTypeDate(serverInfo)
        }
    }

    init {
        if (java.lang.Boolean.TRUE != serverInfo.configure.settings()!![SettingKey.use_client_time_zone]) {
            dateTimeZone = DateTimeZone.forTimeZone(serverInfo.timeZone())
        } else {
            dateTimeZone = DateTimeZone.getDefault()
        }
        dateFormat.timeZone = dateTimeZone!!.toTimeZone()
    }
}