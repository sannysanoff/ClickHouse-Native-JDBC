package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException
import java.sql.Types

class DataTypeFloat64 : IDataType {
    override fun name(): String {
        return "Float64"
    }

    override fun sqlTypeId(): Int {
        return Types.DOUBLE
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Double::class.java
    }

    override fun nullable(): Boolean {
        return false
    }

    
    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeDouble((data as Number).toDouble())
    }

    
    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Double {
        return deserializer.readDouble()
    }

    
    override suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = DoubleArray(rows)
        deserializer.readDoubles(rows);
        return data
    }


    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.numberLiteral().toDouble()
    }

    companion object {
        private const val DEFAULT_VALUE = 0.0
    }
}