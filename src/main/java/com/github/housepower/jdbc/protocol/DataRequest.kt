package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class DataRequest @JvmOverloads constructor(private val name: String, private val block: Block = Block()) : RequestOrResponse(ProtocolType.REQUEST_DATA) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        serializer!!.writeStringBinary(name)
        serializer!!.maybeEnableCompressed()
        block.writeTo(serializer)
        serializer!!.maybeDisableCompressed()
    }

    companion object {
        val EMPTY = DataRequest("")
    }

}