package com.softwareplace.log

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.text.DateFormat
import java.text.SimpleDateFormat


val Any.log: Logger get() = LoggerFactory.getLogger(this::class.java)


private data class LoggerModel(
    val message: String?,
    val properties: Map<String, Any>?,
    val errorMessage: String? = null
)

data class JsonLog(private val logger: Logger) {
    private var message: String? = null
    private var properties: HashMap<String, Any>? = null
    private var error: Throwable? = null


    fun error(error: Throwable?): JsonLog {
        this.error = error
        return this
    }

    fun message(message: String, vararg args: Any?): JsonLog {
        this.message = String.format(message.replace("{}", "%s"), *args)
        return this
    }

    fun add(key: String, value: Any): JsonLog {
        if (properties == null) {
            properties = HashMap()
        }
        properties?.let { it[key] = value }
        return this
    }

    fun log(level: Level) {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm a z")
        val mapper = ObjectMapper()
            .setDateFormat(dateFormat)
            .registerModule(JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

        val loggerMessage = mapper.writeValueAsString(
            LoggerModel(
                message = message,
                properties = properties,
                errorMessage = error?.message
            )
        )
        logger.log(level, loggerMessage)
    }
}

fun Logger.log(
    level: Level = Level.INFO,
    message: String
) {
    when (level) {
        Level.DEBUG -> debug(message)
        Level.TRACE -> trace(message)
        Level.WARN -> warn(message)
        Level.ERROR -> error(message)
        else -> info(message)
    }
}

