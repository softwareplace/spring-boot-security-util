package com.softwareplace.springsecurity.exception

import org.springframework.http.HttpStatus

open class ApiBaseException(
    val status: HttpStatus,
    val info: Map<String, Any> = mapOf(),
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
