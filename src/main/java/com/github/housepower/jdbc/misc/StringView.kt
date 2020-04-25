package com.github.housepower.jdbc.misc

import java.util.*

class StringView(private val start: Int, private val end: Int, private val values: CharArray) {
    fun start(): Int {
        return start
    }

    fun end(): Int {
        return end
    }

    fun values(): CharArray {
        return values
    }

    override fun toString(): String {
        return String(Arrays.copyOfRange(values, start, end))
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is String) {
            val expectString = obj
            if (expectString.length == end - start) {
                for (i in 0 until expectString.length) {
                    if (expectString[i] != values[start + i]) return false
                }
                return true
            }
            return false
        }
        return super.equals(obj)
    }

}