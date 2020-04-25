package com.github.housepower.jdbc

import java.sql.SQLException

class ClickHouseSQLException @JvmOverloads constructor(val code: Int, message: String?, cause: Throwable? = null) : SQLException(message, cause) 