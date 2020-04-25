package com.github.housepower.jdbc.settings

import com.github.housepower.jdbc.serializer.BinarySerializer
import java.io.IOException

interface ISettingType {
    fun deserializeURL(queryParameter: String): Any

    @Throws(IOException::class)
    suspend fun serializeSetting(serializer: BinarySerializer, value: Any)

    companion object {
        @JvmField
        val Int64: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return java.lang.Long.valueOf(queryParameter)
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeVarInt((value as Long))
            }
        }
        @JvmField
        val Int32: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return Integer.valueOf(queryParameter)
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeVarInt((value as Int?)!!.toLong())
            }
        }
        @JvmField
        val Float: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return java.lang.Float.valueOf(queryParameter)
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeStringBinary(value.toString())
            }
        }
        @JvmField
        val String: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return queryParameter
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeStringBinary(value.toString())
            }
        }
        @JvmField
        val Boolean: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return java.lang.Boolean.valueOf(queryParameter)
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeVarInt(if (java.lang.Boolean.TRUE == value) 1 else 0.toLong())
            }
        }
        @JvmField
        val Seconds: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return Integer.valueOf(queryParameter)
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeVarInt((value as Int). toLong ())
            }
        }
        @JvmField
        val Character: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return queryParameter[0]
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeStringBinary(value.toString())
            }
        }
        @JvmField
        val Milliseconds: ISettingType = object : ISettingType {
            override fun deserializeURL(queryParameter: String): Any {
                return java.lang.Long.valueOf(queryParameter)
            }

            @Throws(IOException::class)
            override suspend fun serializeSetting(serializer: BinarySerializer, value: Any) {
                serializer.writeVarInt((value as Long))
            }
        }
    }
}