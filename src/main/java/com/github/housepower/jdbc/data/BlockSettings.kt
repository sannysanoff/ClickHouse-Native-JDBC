package com.github.housepower.jdbc.data

import com.github.housepower.jdbc.serializer.BinaryDeserializer
import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException

class BlockSettings(private val settings: Array<Setting?>) {
    @Throws(IOException::class)
    suspend fun writeTo(serializer: BinarySerializer) {
        for (setting in settings) {
            serializer.writeVarInt(setting!!.num.toLong())
            if (Boolean::class.java.isAssignableFrom(setting.clazz)) {
                serializer.writeBoolean((setting.defaultValue as Boolean))
            } else if (Int::class.java.isAssignableFrom(setting.clazz)) {
                serializer.writeInt((setting.defaultValue as Int))
            }
        }
        serializer.writeVarInt(0)
    }

    class Setting(val num: Int, val defaultValue: Any) {
        val clazz: Class<*>

        companion object {
            val BUCKET_NUM = Setting(2, -1)
            val IS_OVERFLOWS = Setting(1, false)
            fun values(): Array<Setting?> {
                return arrayOf(IS_OVERFLOWS, BUCKET_NUM)
            }
        }

        init {
            clazz = defaultValue.javaClass
        }
    }

    companion object {
        @Throws(IOException::class)
        suspend fun readFrom(deserializer: BinaryDeserializer): BlockSettings {
            return BlockSettings(readSettingsFrom(1, deserializer))
        }

        @Throws(IOException::class)
        private suspend fun readSettingsFrom(currentSize: Int, deserializer: BinaryDeserializer): Array<Setting?> {
            val num = deserializer.readVarInt()
            for (setting in Setting.values()) {
                if (setting!!.num.toLong() == num) {
                    if (Boolean::class.java.isAssignableFrom(setting.clazz)) {
                        val receiveSetting = Setting(setting.num, deserializer.readBoolean())
                        val settings = readSettingsFrom(currentSize + 1, deserializer)
                        settings[currentSize - 1] = receiveSetting
                        return settings
                    } else if (Int::class.java.isAssignableFrom(setting.clazz)) {
                        val receiveSetting = Setting(setting.num, deserializer.readInt())
                        val settings = readSettingsFrom(currentSize + 1, deserializer)
                        settings[currentSize - 1] = receiveSetting
                        return settings
                    }
                }
            }
            return arrayOfNulls(currentSize - 1)
        }
    }

}