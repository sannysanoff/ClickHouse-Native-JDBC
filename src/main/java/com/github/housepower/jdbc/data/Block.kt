package com.github.housepower.jdbc.data

import com.github.housepower.jdbc.connect.PhysicalInfo.ServerInfo
import com.github.housepower.jdbc.misc.Validate
import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException
import java.sql.SQLException
import java.util.*

class Block @JvmOverloads constructor(
        private var rows: Int = 0,
        val columns: Array<Column?> = arrayOfNulls(0),
        private val settings: BlockSettings = BlockSettings(BlockSettings.Setting.values())) {
    private val nameWithPosition: MutableMap<String, Int>
    private val objects: Array<Any?>
    private val columnIndexAdds: IntArray

    
    suspend fun appendRow() {
        var i = 0
        try {
            i = 0
            while (i < columns.size) {
                columns[i]!!.write(objects[i]!!)
                i++
            }
            rows++
        } catch (e: IOException) {
            throw SQLException("Exception processing value " + objects[i] + " for column : " + columns[i]!!.name(), e)
        } catch (e: ClassCastException) {
            throw SQLException("Exception processing value " + objects[i] + " for column : " + columns[i]!!.name(), e)
        }
    }

    fun setObject(i: Int, `object`: Any?) {
        objects[columnIndexAdds[i]] = `object`
    }

    fun setConstObject(i: Int, `object`: Any?) {
        objects[i] = `object`
    }

    fun incrIndex(i: Int) {
        for (j in i until columnIndexAdds.size) {
            columnIndexAdds[j] += 1
        }
    }

    @Throws(IOException::class, SQLException::class)
    suspend fun writeTo(serializer: BinarySerializer) {
        settings.writeTo(serializer)
        serializer.writeVarInt(columns.size.toLong())
        serializer.writeVarInt(rows.toLong())
        for (column in columns) {
            column!!.serializeBinaryBulk(serializer)
        }
    }

    fun rows(): Int {
        return rows
    }

    fun columns(): Int {
        return columns.size
    }

    
    fun getByPosition(column: Int): Column {
        Validate.isTrue(column < columns.size,
                "Position " + column
                        + " is out of bound in Block.getByPosition, max position = " + (columns.size - 1))
        return columns[column]!!
    }

    
    fun getPositionByName(name: String): Int {
        Validate.isTrue(nameWithPosition.containsKey(name), "Column '$name' does not exist")
        return nameWithPosition[name]!!
    }

    
    fun getObject(index: Int): Any? {
        Validate.isTrue(index < columns.size,
                "Position " + index
                        + " is out of bound in Block.getByPosition, max position = " + (columns.size - 1))
        return objects[index]
    }

    fun initWriteBuffer() {
        for (column in columns) {
            column!!.initWriteBuffer()
        }
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class, SQLException::class)
        suspend fun readFrom(deserializer: BinaryDeserializer,
                             serverInfo: ServerInfo?): Block {
            val info = BlockSettings.readFrom(deserializer)
            val columns = deserializer.readVarInt().toInt()
            val rows = deserializer.readVarInt().toInt()
            val cols = arrayOfNulls<Column>(columns)
            for (i in 0 until columns) {
                val name = deserializer.readStringBinary()
                val type = deserializer.readStringBinary()
                val dataType = DataTypeFactory.get(type, serverInfo)
                val arr = dataType.deserializeBinaryBulk(rows, deserializer)
                cols[i] = Column(name, dataType, arr)
            }
            return Block(rows, cols, info)
        }
    }

    init {
        objects = arrayOfNulls(columns.size)
        columnIndexAdds = IntArray(columns.size)
        nameWithPosition = HashMap()
        for (i in columns.indices) {
            nameWithPosition[columns[i]!!.name()] = i + 1
            columnIndexAdds[i] = i
        }
    }
}