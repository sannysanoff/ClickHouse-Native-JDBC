package com.github.housepower.jdbc

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.wrapper.SQLResultSetMetaData
import java.sql.SQLException

class ClickHouseResultSetMetaData(private val header: Block) : SQLResultSetMetaData() {
    
    override fun getColumnCount(): Int {
        return header.columns()
    }

    
    override fun getColumnType(index: Int): Int {
        val type = header.getByPosition(index - 1).type()
        return type.sqlTypeId()
    }

    
    override fun isCaseSensitive(column: Int): Boolean {
        return true
    }

    
    override fun isSearchable(column: Int): Boolean {
        return true
    }

    
    override fun isCurrency(column: Int): Boolean {
        return false
    }

    
    override fun getColumnDisplaySize(column: Int): Int {
        return 80
    }

    
    override fun getColumnTypeName(column: Int): String {
        return header.getByPosition(column - 1).type().name()
    }

    
    override fun getColumnClassName(column: Int): String {
        return header.getByPosition(column - 1).type().javaTypeClass().name
    }

    
    override fun getColumnName(index: Int): String {
        return getColumnLabel(index)
    }

    
    override fun getColumnLabel(index: Int): String {
        return header.getByPosition(index - 1).name()
    }

    
    override fun isNullable(index: Int): Int {
        throw SQLException("NYI")
        //        return (header.getByPosition(index - 1).type() instanceof DataTypeNullable) ?
//            ResultSetMetaData.columnNullable : ResultSetMetaData.columnNoNulls;
    }

    
    override fun isSigned(index: Int): Boolean {
        return header.getByPosition(index).name().startsWith("U")
    }

    /*=========================================================*/
    
    override fun isReadOnly(index: Int): Boolean {
        return true
    }

    
    override fun isWritable(index: Int): Boolean {
        return false
    }

    
    override fun isAutoIncrement(index: Int): Boolean {
        return false
    }

}