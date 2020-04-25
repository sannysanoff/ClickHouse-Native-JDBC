package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.ClickHouseSQLException
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class ExceptionResponse internal constructor() : RequestOrResponse(ProtocolType.RESPONSE_Exception) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("ExceptionResponse Cannot write to Server.")
    }

    companion object {
        @Throws(IOException::class)
        suspend fun readExceptionFrom(deserializer: BinaryDeserializer): SQLException {
            val code = deserializer.readInt()
            val name = deserializer.readStringBinary()
            val message = deserializer.readStringBinary()
            val stackTrace = deserializer.readStringBinary()
            return if (deserializer.readBoolean()) {
                ClickHouseSQLException(
                        code, "$name$message. Stack trace:\n\n$stackTrace", readExceptionFrom(deserializer))
            } else ClickHouseSQLException(code, "$name$message. Stack trace:\n\n$stackTrace")
        }
    }
}