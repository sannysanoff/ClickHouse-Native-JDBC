package com.github.housepower.jdbc.data

import com.github.housepower.jdbc.buffer.ByteArrayWriter
import com.github.housepower.jdbc.serializer.BinarySerializer
import com.github.housepower.jdbc.settings.ClickHouseDefines
import java.io.IOException

/**
 *
 */
class ColumnWriterBuffer {
    var column: BinarySerializer
    private val columnWriter: ByteArrayWriter

    suspend fun writeTo(serializer: BinarySerializer) {
        for (buffer in columnWriter.bufferList) {
            buffer.flip()
            while (buffer.hasRemaining()) {
                serializer.writeByte(buffer.get())
            }
        }
    }

    init {
        columnWriter = ByteArrayWriter(ClickHouseDefines.COLUMN_BUFFER)
        column = BinarySerializer(columnWriter, false)
    }
}