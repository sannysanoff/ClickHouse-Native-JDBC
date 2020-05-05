package com.github.housepower.jdbc.connect

import com.github.housepower.jdbc.buffer.SocketBuffedWriter
import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.Validate.isTrue
import com.github.housepower.jdbc.protocol.*
import com.github.housepower.jdbc.protocol.QueryRequest.ClientInfo
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import com.github.housepower.jdbc.settings.ClickHouseConfig
import com.github.housepower.jdbc.settings.SettingKey
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.sql.SQLException
import java.util.*

class PhysicalConnection(val socket: Socket,
                         val serializer: BinarySerializer,
                         val deserializer: BinaryDeserializer,
                         val address: SocketAddress) {
    suspend fun ping(soTimeout: Int, info: ServerInfo?): Boolean {
        try {
            sendRequest(PingRequest())
            while (true) {
                val response = receiveResponse(soTimeout, info)
                if (response is EOFStreamResponse) {
                    continue
                }
                isTrue(response is ProgressResponse || response is PongResponse,
                        "Expect Pong Response.")
                if (response is PongResponse) return true
            }
        } catch (e: SQLException) {
            return false
        }
    }


    suspend fun sendData(data: Block) {
        sendRequest(DataRequest("", data))
    }


    suspend fun sendQuery(query: String, info: ClientInfo, settings: Map<SettingKey, Any>) {
        sendQuery(UUID.randomUUID().toString(), QueryRequest.COMPLETE_STAGE, info, query, settings)
    }


    suspend fun sendHello(client: String, reversion: Long, db: String, user: String, password: String) {
        sendRequest(HelloRequest(client, reversion, db, user, password))
    }


    suspend fun receiveSampleBlock(soTimeout: Int, info: ServerInfo?): Block {
        while (true) {
            val response = receiveResponse(soTimeout, info)
            if (response is DataResponse) {
                return response.block()
            }
        }
    }


    suspend fun receiveHello(soTimeout: Int, info: ServerInfo?): HelloResponse {
        val response = receiveResponse(soTimeout, info)
        isTrue(response is HelloResponse, "Expect Hello Response.")
        return response as HelloResponse
    }


    suspend fun receiveEndOfStream(soTimeout: Int, info: ServerInfo?): EOFStreamResponse {
        val response = receiveResponse(soTimeout, info)
        isTrue(response is EOFStreamResponse, "Expect EOFStream Response.")
        return response as EOFStreamResponse
    }


    suspend fun receiveResponse(soTimeout: Int, info: ServerInfo?): RequestOrResponse {
        return try {
//            socket.soTimeout = soTimeout
            val t1 = System.currentTimeMillis()
            val rv = RequestOrResponse.readFrom(deserializer, info)
            val t2 = System.currentTimeMillis()
            var stat = ""
            if (rv is DataResponse) {
                stat = "" + rv.block().rows()
            }
            //            System.out.println(System.currentTimeMillis()+" Refill time: "+(t2-t1)+" blocksize="+stat);
            rv
        } catch (ex: IOException) {
            throw SQLException(ex.message, ex)
        }
    }

    fun address(): SocketAddress {
        return address
    }


    suspend fun disPhysicalConnection() {
        try {
            serializer.flushToTarget(true)
            socket.close()
        } catch (ex: IOException) {
            throw SQLException(ex.message, ex)
        }
    }


    private suspend fun sendQuery(id: String, stage: Int, info: ClientInfo, query: String,
                                  settings: Map<SettingKey, Any>) {
        sendRequest(QueryRequest(id, info, stage, false, query, settings))
    }


    private suspend fun sendRequest(request: RequestOrResponse) {
        try {
            println("ClickHouse: Sending: ${request.javaClass}")
            request.writeTo(serializer)
            serializer.flushToTarget(true)
            println("ClickHouse: Flushed after: ${request.javaClass}")
        } catch (ex: IOException) {
            throw SQLException(ex.message, ex)
        }
    }

    companion object {

        val asm = ActorSelectorManager(Dispatchers.IO);

        suspend fun openPhysicalConnection(configure: ClickHouseConfig): PhysicalConnection {
            return try {
                val endpoint: SocketAddress = InetSocketAddress(configure.address(), configure.port())
                val socket = aSocket(asm).tcp().connect(endpoint)
//                socket.tcpNoDelay = true
//                socket.sendBufferSize = ClickHouseDefines.SOCKET_BUFFER_SIZE
//                socket.receiveBufferSize = ClickHouseDefines.SOCKET_BUFFER_SIZE
//                socket.connect(endpoint, configure.connectTimeout())
                PhysicalConnection(socket, BinarySerializer(SocketBuffedWriter(socket), false), BinaryDeserializer(socket), endpoint)
            } catch (ex: IOException) {
                throw SQLException(ex.message, ex)
            }
        }
    }

}