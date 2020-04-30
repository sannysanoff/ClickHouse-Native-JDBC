package com.github.housepower.jdbc.data

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.type.*
import com.github.housepower.jdbc.data.type.DataTypeDate.Companion.createDateType
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.misc.Validate.isTrue
import java.util.*

object DataTypeFactory {
    
    operator fun get(type: String?, serverInfo: ServerInfo?): IDataType {
        val lexer = SQLLexer(0, type!!)
        val dataType = get(lexer, serverInfo)
        isTrue(lexer.eof())
        return dataType!!
    }

    private val dataTypes = initialDataTypes()

    operator fun get(lexer: SQLLexer, serverInfo: ServerInfo?): IDataType? {
        val dataTypeName = lexer.bareWord()
        return if (dataTypeName.equals("Date")) {
            createDateType(lexer, serverInfo!!)
            //        } else if (dataTypeName.equals("Tuple")) {
//            return DataTypeTuple.createTupleType(lexer, serverInfo);
//        } else if (dataTypeName.equals("Array")) {
//            return DataTypeArray.createArrayType(lexer, serverInfo);
//        } else if (dataTypeName.equals("Enum8")) {
//            return DataTypeEnum8.createEnum8Type(lexer, serverInfo);
//        } else if (dataTypeName.equals("Enum16")) {
//            return DataTypeEnum16.createEnum16Type(lexer, serverInfo);
        } else if (dataTypeName.equals("DateTime")) {
            return DataTypeDateTime.createDateTimeType(lexer, serverInfo!!);
//        } else if (dataTypeName.equals("DateTime64")) {
//            return DataTypeDateTime64.createDateTime64Type(lexer, serverInfo);
//        } else if (dataTypeName.equals("Nullable")) {
//            return DataTypeNullable.createNullableType(lexer, serverInfo);
//        } else if (dataTypeName.equals("FixedString")) {
//            return DataTypeFixedString.createFixedStringType(lexer, serverInfo);
//        } else if (dataTypeName.equals("Decimal")) {
//            return DataTypeDecimal.createDecimalType(lexer, serverInfo);
        } else {
            val name = dataTypeName.toString()
            val dataType = dataTypes[name]
            isTrue(dataType != null, "Unknown data type family:$name")
            dataType
        }
    }

    private fun initialDataTypes(): Map<String, IDataType> {
        val creators: MutableMap<String, IDataType> = HashMap()
        creators["String"] = DataTypeString()
        creators["Float32"] = DataTypeFloat32()
        creators["Float64"] = DataTypeFloat64()
        creators["Int8"] = DataTypeInt8("Int8")
        creators["Int16"] = DataTypeInt16("Int16")
        creators["Int32"] = DataTypeInt32("Int32")
        creators["Int64"] = DataTypeInt64("Int64")
        creators["UInt8"] = DataTypeInt8("UInt8")
        creators["UInt16"] = DataTypeInt16("UInt16")
        creators["UInt32"] = DataTypeInt32("UInt32")
        creators["UInt64"] = DataTypeInt64("UInt64")
        return creators
    }
}