package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.sql.SQLException
import java.sql.Types

class DataTypeInt8(private val name: String) : IDataType {
    private val isUnsigned: Boolean
    override fun name(): String {
        return name
    }

    override fun sqlTypeId(): Int {
        return Types.TINYINT
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Byte::class.java
    }

    override fun nullable(): Boolean {
        return false
    }


    override suspend  fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeByte((data as Number).toByte())
    }


    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Number {
        val b = deserializer.readByte()
        return if (isUnsigned) {
            (b.toInt() and 0xff)
        } else b
    }


    override suspend  fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = arrayOfNulls<Number>(rows)
        for (row in 0 until rows) {
            data[row] = deserializeBinary(deserializer)
        }
        return data
    }

    @Throws(SQLException::class)
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.numberLiteral().toByte()
    }

    companion object {
        private const val DEFAULT_VALUE: Byte = 0
    }

    init {
        isUnsigned = name.startsWith("U")
    }
}