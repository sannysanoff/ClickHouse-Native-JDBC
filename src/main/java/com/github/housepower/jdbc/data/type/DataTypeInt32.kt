package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.sql.SQLException
import java.sql.Types

class DataTypeInt32(private val name: String) : IDataType {
    private val isUnsigned: Boolean
    override fun name(): String {
        return name
    }

    override fun sqlTypeId(): Int {
        return Types.INTEGER
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Int::class.java
    }

    override fun nullable(): Boolean {
        return false
    }


    override suspend  fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeInt((data as Number).toInt())
    }


    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        val res = deserializer.readInt()
        return if (isUnsigned) {
            0xffffffffL and res.toLong()
        } else res
    }


    override suspend  fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = IntArray(rows)
        for (row in 0 until rows) {
            data[row] = deserializer.readInt()
        }
        return data
    }

    @Throws(SQLException::class)
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.numberLiteral().toLong() and -0x1
    }

    companion object {
        private const val DEFAULT_VALUE = 0
    }

    init {
        isUnsigned = name.startsWith("U")
    }
}