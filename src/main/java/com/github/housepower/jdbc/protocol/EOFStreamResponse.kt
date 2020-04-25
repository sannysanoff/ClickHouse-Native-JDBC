package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException

class EOFStreamResponse internal constructor() : RequestOrResponse(ProtocolType.RESPONSE_EndOfStream) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        throw UnsupportedOperationException("EndOfStreamResponse Cannot write to Server.")
    }

    companion object {
        fun readFrom(deserializer: BinaryDeserializer?): RequestOrResponse {
            return EOFStreamResponse()
        }
    }
}