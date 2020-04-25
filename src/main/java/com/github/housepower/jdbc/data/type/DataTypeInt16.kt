package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.sql.Types

class DataTypeInt16(private val name: String) : IDataType {
    private val isUnsigned: Boolean
    override fun name(): String {
        return name
    }

    override fun sqlTypeId(): Int {
        return Types.SMALLINT
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Short::class.java
    }

    override fun nullable(): Boolean {
        return false
    }

    
    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeShort((data as Number).toShort())
    }


    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        val s = deserializer.readShort().toLong()
        return if (isUnsigned) {
            s and 0xffffL
        } else s
    }


    override suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = arrayOfNulls<Any>(rows)
        for (row in 0 until rows) {
            data[row] = deserializeBinary(deserializer)
        }
        return data
    }


    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.numberLiteral().toShort()
    }

    companion object {
        private const val DEFAULT_VALUE: Short = 0
    }

    init {
        isUnsigned = name.startsWith("U")
    }
}