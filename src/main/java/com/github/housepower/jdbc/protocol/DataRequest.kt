package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException

class DataRequest @JvmOverloads constructor(val name: String, val block: Block = Block()) : RequestOrResponse(ProtocolType.REQUEST_DATA) {
    override suspend fun writeImpl(serializer: BinarySerializer) {
        serializer!!.writeStringBinary(name)
        serializer!!.maybeEnableCompressed()
        block.writeTo(serializer)
        serializer!!.maybeDisableCompressed()
    }

    companion object {
        val EMPTY = DataRequest("")
    }

    override fun toString(): String {
        return super.toString()+": block name="+name+" blockRows="+block.rows()+" cols="+block.columns.size
    }
}