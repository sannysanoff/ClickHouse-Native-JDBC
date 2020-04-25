package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException
import java.sql.Types

class DataTypeString : IDataType {
    override fun name(): String {
        return "String"
    }

    override fun sqlTypeId(): Int {
        return Types.VARCHAR
    }

    override fun defaultValue(): Any {
        return ""
    }

    override fun javaTypeClass(): Class<*> {
        return String::class.java
    }

    override fun nullable(): Boolean {
        return false
    }


    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeStringBinary((data as String))
    }


    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        return deserializer.readStringBinary()
    }

    
    override suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = arrayOfNulls<String>(rows)
        for (row in 0 until rows) {
            data[row] = deserializer.readStringBinary()
        }
        return data
    }

    
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.stringLiteral().toString()
    }
}