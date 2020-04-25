package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException

class PingRequest : RequestOrResponse(ProtocolType.REQUEST_PING) {
    suspend override fun writeImpl(serializer: BinarySerializer) {
        //Nothing
    }
}