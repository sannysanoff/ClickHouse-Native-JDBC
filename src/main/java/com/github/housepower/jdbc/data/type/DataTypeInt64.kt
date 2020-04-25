package com.github.housepower.jdbc.data.type

import com.github.housepower.jdbc.data.IDataType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.sql.SQLException
import java.sql.Types

class DataTypeInt64(private val name: String) : IDataType {
    private val isUnsigned: Boolean
    override fun name(): String {
        return name
    }

    override fun sqlTypeId(): Int {
        return Types.BIGINT
    }

    override fun defaultValue(): Any {
        return DEFAULT_VALUE
    }

    override fun javaTypeClass(): Class<*> {
        return Long::class.java
    }

    override fun nullable(): Boolean {
        return false
    }

    override suspend fun serializeBinary(data: Any, serializer: BinarySerializer) {
        serializer.writeLong((data as Number).toLong())
    }

    
    override suspend fun deserializeBinary(deserializer: BinaryDeserializer): Any {
        val l = deserializer.readLong()
        return if (isUnsigned) {
            BigInteger(1, longToBytes(l))
        } else l
    }

    
    override suspend fun deserializeBinaryBulk(rows: Int, deserializer: BinaryDeserializer): Any {
        val data = LongArray(rows)
        for (row in 0 until rows) {
            data[row] = deserializer.readLong()
        }
        return data
    }

    @Throws(SQLException::class)
    override fun deserializeTextQuoted(lexer: SQLLexer): Any {
        return lexer.numberLiteral().toLong()
    }

    companion object {
        private const val DEFAULT_VALUE = 0L
        private val TWO_COMPL_REF = BigInteger.ONE.shiftLeft(64)
        private fun longToBytes(x: Long): ByteArray {
            val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)
            buffer.putLong(x)
            return buffer.array()
        }

        fun parseBigIntegerPositive(num: String?, bitlen: Int): BigInteger {
            var b = BigInteger(num)
            if (b.compareTo(BigInteger.ZERO) < 0) {
                b = b.add(BigInteger.ONE.shiftLeft(bitlen))
            }
            return b
        }
    }

    init {
        isUnsigned = name.startsWith("U")
    }
}