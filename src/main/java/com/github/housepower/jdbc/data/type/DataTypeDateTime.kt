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
import java.sql.SQLException
import java.sql.Timestamp
import java.sql.Types
import java.text.ParseException
import java.text.SimpleDateFormat


class DataTypeDateTime(private val name: String, serverInfo: ServerInfo) : IDataType {
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun name(): String {
        return name
    }

    override fun sqlTypeId(): Int {
        return Types.TIMESTAMP
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Timestamp::class.java
    }

    override fun nullable(): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        isTrue(lexer.character() == '\'')
        val year: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == '-')
        val month: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == '-')
        val day: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.isWhitespace)
        val hours: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == ':')
        val minutes: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == ':')
        val seconds: Int = lexer.numberLiteral().toInt()
        isTrue(lexer.character() == '\'')
        val timeStr = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hours, minutes, seconds)
        return try {
            val date = dateTimeFormat.parse(timeStr)
            Timestamp(date.time)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeInt(((data as Timestamp).time / 1000).toInt())
    }

    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        return Timestamp(deserializer.readInt() * 1000L)
    }

    override suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Array<Any> {
        val data = arrayOfNulls<Timestamp>(rows)
        for (row in 0 until rows) {
            data[row] = Timestamp(deserializer.readInt() * 1000L)
        }
        return data as Array<Any>
    }

    companion object {
        private val DEFAULT_VALUE = Timestamp(0)

        @Throws(SQLException::class)
        fun createDateTimeType(lexer: SQLLexer, serverInfo: ServerInfo): IDataType {
            if (lexer.isCharacter('(')) {
                isTrue(lexer.character() == '(')
                val dataTimeZone = lexer.stringLiteral()
                isTrue(lexer.character() == ')')
                return DataTypeDateTime("DateTime(" +
                        dataTimeZone + ")", serverInfo)
            }
            return DataTypeDateTime("DateTime", serverInfo)
        }
    }

    init {
        if (java.lang.Boolean.TRUE != serverInfo.configure.settings()!![SettingKey.use_client_time_zone]) {
            dateTimeFormat.timeZone = serverInfo.timeZone()
        } else {
            dateTimeFormat.timeZone = DateTimeZone.getDefault().toTimeZone()
        }
    }
}