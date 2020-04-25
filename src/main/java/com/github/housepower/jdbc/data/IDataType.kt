package com.github.housepower.jdbc.data

import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

interface IDataType {
    fun name(): String
    fun sqlTypeId(): Int
    fun defaultValue(): Any
    fun javaTypeClass(): Class<*>
    fun nullable(): Boolean

    
    fun deserializeTextQuoted(lexer: SQLLexer): Any

    
    suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any

    
    suspend fun serializeBinary(data: Any, serializer: BinarySerializer)

    
    suspend fun serializeBinaryBulk(data: Iterator<Any>, serializer: BinarySerializer) {
        while (data.hasNext()) {
            serializeBinary(data.next(), serializer)
        }
    }

    
    suspend fun serializeBinaryBulk(data: Array<Any?>, serializer: BinarySerializer) {
        for (d in data) {
            serializeBinary(d!!, serializer)
        }
    }

    
    suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any?
}