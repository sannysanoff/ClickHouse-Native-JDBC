package com.github.housepower.jdbc.misc

import java.sql.SQLException

object Validate {
    @JvmStatic
    fun isTrue(expression: Boolean) {
        isTrue(expression, null)
    }

    @JvmStatic
    fun isTrue(expression: Boolean, message: String?) {
        if (!expression) {
            throw SQLException(message)
        }
    }
}