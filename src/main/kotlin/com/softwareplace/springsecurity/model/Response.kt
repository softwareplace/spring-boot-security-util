package com.softwareplace.springsecurity.model


data class Response(
    val timestamp: Long = System.currentTimeMillis(),
    val message: String = "Could not complete the request, try again soon",
    val success: Boolean = false,
    val info: Map<String, Any> = mapOf()
)

