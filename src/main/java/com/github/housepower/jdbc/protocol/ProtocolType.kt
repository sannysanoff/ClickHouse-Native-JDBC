package com.github.housepower.jdbc.protocol

enum class ProtocolType(private val id: Int) {
    REQUEST_HELLO(0), REQUEST_QUERY(1), REQUEST_DATA(2), REQUEST_PING(4), RESPONSE_HELLO(0), RESPONSE_Data(1), RESPONSE_Exception(2), RESPONSE_Progress(3), RESPONSE_Pong(4), RESPONSE_EndOfStream(5), RESPONSE_ProfileInfo(6), RESPONSE_Totals(7), RESPONSE_Extremes(8), RESPONSE_TablesStatusResponse(9);

    fun id(): Long {
        return id.toLong()
    }

}