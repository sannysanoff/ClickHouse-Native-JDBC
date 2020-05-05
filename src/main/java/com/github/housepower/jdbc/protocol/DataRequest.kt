package com.github.housepower.jdbc.protocol

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.lang.Exception
import java.sql.SQLException
import java.util.concurrent.atomic.AtomicInteger

val dataRequestSeq = AtomicInteger(0);

class DataRequest @JvmOverloads constructor(val name: String, val block: Block = Block()) : RequestOrResponse(ProtocolType.REQUEST_DATA) {

    companion object {
        val EMPTY = DataRequest("")
    }

    init {
        val sequence = dataRequestSeq.incrementAndGet();
        println("${sequence}: DataRequest: thread="+Thread.currentThread());
        Exception("${sequence}: NOT an exception").printStackTrace();
    }
    override suspend fun writeImpl(serializer: BinarySerializer) {
        serializer!!.writeStringBinary(name)
        serializer!!.maybeEnableCompressed()
        block.writeTo(serializer)
        serializer!!.maybeDisableCompressed()
    }

    override fun toString(): String {
        return super.toString()+": block name="+name+" blockRows="+block.rows()+" cols="+block.columns.size
    }
}