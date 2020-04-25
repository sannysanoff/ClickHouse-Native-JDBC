package com.github.housepower.jdbc

import com.github.housepower.jdbc.wrapper.SQLArray
import java.lang.reflect.Array
import java.sql.SQLException

class ClickHouseArray(private val data: Any) : SQLArray() {
    
    override fun free() {
    }

    
    override fun getArray(): Any {
        return data
    }

    fun slice(offset: Int, length: Int): ClickHouseArray {
        val result = arrayOfNulls<Any>(length)
        for (i in 0 until length) {
            result[i] = Array.get(data, i + offset)
        }
        return ClickHouseArray(result)
    }

}