package com.github.housepower.jdbc

import java.sql.DriverManager
import java.sql.SQLException

class ClickHouseDriver : NonRegisterDriver() {
    init {
        try {
//            DriverManager.registerDriver(ClickHouseDriver())
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}