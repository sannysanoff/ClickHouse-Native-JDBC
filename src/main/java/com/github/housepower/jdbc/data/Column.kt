package com.github.housepower.jdbc.data


import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException
import java.sql.Types
import java.util.*

class Column {
    private val name: String
    private val type: IDataType
    var values: Any? = null
    var valuesLong: LongArray? = null
    var valuesDouble // see also: getArray
            : DoubleArray? = null
    var valuesInt // see also: getArray
            : IntArray? = null
    var buffer: ColumnWriterBuffer? = null
        private set
    private var isArray = false
    private var offsets: List<List<Int>>? = null

    constructor(name: String, type: IDataType) {
        this.name = name
        this.type = type
    }

    fun getArray(): Any? {
        if (valuesDouble != null) return valuesDouble
        if (valuesLong != null) return valuesLong
        if (valuesInt != null) return valuesInt
        val comp = values!!.javaClass.componentType
        if (comp == Double::class.java) {
            val retval = DoubleArray(java.lang.reflect.Array.getLength(values))
            val arr = values as Array<Double>?
            for (i in retval.indices) {
                retval[i] = if (arr!![i] != null) arr[i] else Double.NaN
            }
            return retval
        }
        if (comp == Long::class.java) {
            val retval = DoubleArray(java.lang.reflect.Array.getLength(values))
            val arr = values as Array<Long>?
            for (i in retval.indices) {
                retval[i] = (if (arr!![i] != null) arr[i] else Long.MIN_VALUE).toDouble()
            }
            return retval
        }
        if (comp == Int::class.java) {
            val retval = DoubleArray(java.lang.reflect.Array.getLength(values))
            val arr = values as Array<Int>?
            for (i in retval.indices) {
                retval[i] = (if (arr!![i] != null) arr[i] else Int.MIN_VALUE).toDouble()
            }
            return retval
        }
//        if (!(values is Array<*>)) {
//            println("WARNING: class of values: " + values!!.javaClass.name)
//        }
        return values
    }

    constructor(name: String, type: IDataType, values: Any?) {
        this.values = values
        if (values is LongArray) {
            valuesLong = values
        } else if (values is DoubleArray) {
            valuesDouble = values
        } else if (values is IntArray) {
            valuesInt = values
        }
        this.name = name
        this.type = type
        if (this.type.sqlTypeId() == Types.ARRAY) {
            isArray = true
            offsets = ArrayList()
        }
    }

    fun initWriteBuffer() {
        buffer = ColumnWriterBuffer()
    }

    fun name(): String {
        return name
    }

    fun type(): IDataType {
        return type
    }

    fun values(rowIdx: Int): Any? {
        return java.lang.reflect.Array.get(values, rowIdx)
    }

    fun longs(rowIdx: Int): Long {
        return valuesLong!![rowIdx]
    }

    fun doubles(rowIdx: Int): Double {
        return valuesDouble!![rowIdx]
    }

    fun ints(rowIdx: Int): Int {
        return valuesInt!![rowIdx]
    }

    
    suspend fun write(object0: Any) {
        if (isArray) {
            throw SQLException("NYI")
//            val typ = type() as DataTypeArray
//            typ.serializeBinary(object0, buffer!!.column, offsets, 1)
        } else {
            type().serializeBinary(object0, buffer!!.column)
        }
    }

    
    suspend fun serializeBinaryBulk(serializer: BinarySerializer) {
        serializer.writeStringBinary(name)
        serializer.writeStringBinary(type.name())

        //writer offsets
        if (offsets != null) {
            for (offsetList in offsets!!) {
                for (offset in offsetList) {
                    serializer.writeLong(offset.toLong())
                }
            }
        }
        buffer!!.writeTo(serializer)
    }

}