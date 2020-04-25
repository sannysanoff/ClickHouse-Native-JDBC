package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.sql.SQLException
import java.sql.Types

class DataTypeFloat32 : IDataType {
    override fun name(): String {
        return "Float32"
    }

    override fun sqlTypeId(): Int {
        return Types.FLOAT
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Float::class.java
    }

    override fun nullable(): Boolean {
        return false
    }


    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeFloat((data as Float?)!!)
    }


    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        return deserializer.readFloat()
    }

    
    override suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = arrayOfNulls<Float>(rows)
        for (row in 0 until rows) {
            data[row] = deserializer.readFloat()
        }
        return data
    }

    @Throws(SQLException::class)
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.numberLiteral().toFloat()
    }

    companion object {
        private const val DEFAULT_VALUE = 0.0f
    }
}