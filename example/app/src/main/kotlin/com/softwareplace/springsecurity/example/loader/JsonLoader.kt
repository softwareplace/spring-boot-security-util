package com.softwareplace.springsecurity.example.loader

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.IOException

class JsonLoader {
    private val mapper: ObjectMapper = ObjectMapperFactory.factory()

    companion object {
        val loader = JsonLoader()
    }

    @Throws(IOException::class)
    fun <T> fromJson(jsonFilePath: String, type: TypeReference<T>): T {
        val stream = javaClass.classLoader.getResourceAsStream(jsonFilePath)
        return mapper.readValue(stream, type)
    }

    @Throws(IOException::class)
    fun <T> fromJson(jsonFilePath: String, mapper: ObjectMapper, type: TypeReference<T>): T {
        val stream = javaClass.classLoader.getResourceAsStream(jsonFilePath)
        return mapper.readValue(stream, type)
    }

    @Throws(IOException::class)
    fun <T> fromJson(jsonFilePath: String, tClass: Class<T>): T {
        val stream = javaClass.classLoader.getResourceAsStream(jsonFilePath)
        return mapper.readValue(stream, tClass)
    }
}

class ObjectMapperFactory {
    companion object {
        fun factory(): ObjectMapper {
            return ObjectMapper()
                .registerModule(
                    KotlinModule.Builder()
                        .configure(KotlinFeature.NullToEmptyCollection, false)
                        .configure(KotlinFeature.NullToEmptyMap, false)
                        .configure(KotlinFeature.NullIsSameAsDefault, true)
                        .configure(KotlinFeature.SingletonSupport, true)
                        .configure(KotlinFeature.StrictNullChecks, true)
                        .build()
                )
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(JavaTimeModule())
        }
    }
}
