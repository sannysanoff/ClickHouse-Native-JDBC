package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer

abstract class RequestOrResponse internal constructor(private val type: ProtocolType) {
    fun type(): ProtocolType {
        return type
    }


    suspend fun writeTo(serializer: BinarySerializer) {
        serializer.writeVarInt(type.id())
        writeImpl(serializer)
    }

    abstract suspend fun writeImpl(serializer: BinarySerializer)

    companion object {

        suspend fun readFrom(deserializer: BinaryDeserializer, info: ServerInfo?): RequestOrResponse {
            val rdi = deserializer.readVarInt().toInt()
            return when (rdi) {
                0 -> HelloResponse.readFrom(deserializer)
                1 -> DataResponse.readFrom(deserializer, info)
                2 -> throw ExceptionResponse.readExceptionFrom(deserializer)
                3 -> ProgressResponse.readFrom(deserializer)
                4 -> PongResponse.readFrom(deserializer)
                5 -> EOFStreamResponse.readFrom(deserializer)
                6 -> ProfileInfoResponse.readFrom(deserializer)
                7 -> TotalsResponse.readFrom(deserializer, info)
                8 -> ExtremesResponse.readFrom(deserializer, info)
                else -> throw IllegalStateException("Accept the id of response that is not recognized by Server.")
            }
        }

        private fun isPingResult(type: ProtocolType, deserializer: BinaryDeserializer): Boolean {
            return ProtocolType.REQUEST_PING == type
        }

        private fun isResultPacket(type: ProtocolType, deserializer: BinaryDeserializer): Boolean {
            return ProtocolType.REQUEST_QUERY == type
        }
    }

}