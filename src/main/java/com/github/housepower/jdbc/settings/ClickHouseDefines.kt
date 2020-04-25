package com.github.housepower.jdbc.settings

object ClickHouseDefines {
    const val NAME = "ClickHouse"
    const val DEFAULT_DATABASE = "default"
    const val MAJOR_VERSION = 1
    const val MINOR_VERSION = 1
    const val CLIENT_REVERSION = 54380
    const val DBMS_MIN_REVISION_WITH_SERVER_TIMEZONE = 54058
    const val DBMS_MIN_REVISION_WITH_SERVER_DISPLAY_NAME = 54372
    const val MAX_BLOCK_SIZE = 1048576 * 10
    var SOCKET_BUFFER_SIZE = 1048576
    var COLUMN_BUFFER = 1048576
}