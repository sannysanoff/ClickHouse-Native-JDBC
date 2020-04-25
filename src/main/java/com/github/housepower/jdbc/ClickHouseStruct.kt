package com.github.housepower.jdbc

import com.github.housepower.jdbc.misc.Validate.isTrue
import com.github.housepower.jdbc.wrapper.SQLStruct
import java.sql.SQLException
import java.util.regex.Pattern

class ClickHouseStruct(private val type: String, private val attributes: Array<Any>) : SQLStruct() {
    @Throws(SQLException::class)
    override fun getSQLTypeName(): String {
        return type
    }

    @Throws(SQLException::class)
    override fun getAttributes(): Array<Any> {
        return attributes
    }

    @Throws(SQLException::class)
    override fun getAttributes(map: Map<String, Class<*>>): Array<Any?> {
        var i = 0
        val res = arrayOfNulls<Any>(map.size)
        for (attrName in map.keys) {
            val clazz = map[attrName]!!
            val matcher = REGEX.matcher(attrName)
            isTrue(matcher.matches(), "Can't find $attrName.")
            val attrIndex = matcher.group(1).toInt() - 1
            isTrue(attrIndex < attributes.size, "Can't find $attrName.")
            isTrue(clazz.isInstance(attributes[attrIndex]),
                    "Can't cast " + attrName + " to " + clazz.name)
            res[i++] = clazz.cast(attributes[attrIndex])
        }
        return res
    }

    companion object {
        private val REGEX = Pattern.compile("\\_(\\d+)")
    }

}