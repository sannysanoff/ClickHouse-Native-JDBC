package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.CheckedIterator
import com.github.housepower.jdbc.misc.CheckedSupplier
import java.sql.SQLException
import java.util.function.Supplier

class QueryResponse // Progress
// Totals
// Extremes
// ProfileInfo
// EndOfStream
(private val responseSupplier: CheckedSupplier<RequestOrResponse, SQLException>) {
    private var header: Block? = null
    private var atEnd = false

    @Throws(SQLException::class)
    fun header(): Block? {
        ensureHeaderConsumed()
        return header
    }

    @Throws(SQLException::class)
    private fun ensureHeaderConsumed() {
        if (header == null) {
            val firstDataResponse = consumeDataResponse()
            header = if (firstDataResponse != null) firstDataResponse.block() else Block()
        }
    }

    @Throws(SQLException::class)
    private fun consumeDataResponse(): DataResponse? {
        while (!atEnd) {
            val response = responseSupplier.get()
            if (response is DataResponse) {
                return response
            } else if (response is EOFStreamResponse || response == null) {
                atEnd = true
            }
        }
        return null
    }

    fun data(): Supplier<CheckedIterator<DataResponse, SQLException>> {
        return label@ Supplier {
            object : CheckedIterator<DataResponse, SQLException> {
                var current: DataResponse? = null

                @Throws(SQLException::class)
                private fun fill(): DataResponse? {
                    ensureHeaderConsumed()
                    return@label consumeDataResponse().also { current = it }
                }

                @Throws(SQLException::class)
                private fun drain(): DataResponse {
                    if (current == null) {
                        fill()
                    }
                    val top = current
                    current = null
                    return@label top
                }

                @Throws(SQLException::class)
                override fun hasNext(): Boolean {
                    return@label current != null || fill() != null
                }

                @Throws(SQLException::class)
                override fun next(): DataResponse {
                    return@label drain()
                }
            }
        }
    }

}