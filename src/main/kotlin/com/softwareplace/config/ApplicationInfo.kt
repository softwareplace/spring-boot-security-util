package com.softwareplace.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security-util")
class ApplicationInfo {
    lateinit var securitySecret: String
    var openUrl: List<String> = emptyList()
    var allowedHeaders: List<String> = listOf("Access-Control-Allow-Headers", "Authorization", "Content-Type", "authentication")
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    var allowedOrigins: List<String> = listOf("*")
    var corsConfigPattern: String = "/**"
    var jwtExpirationTime: Long = 7776000000L
}
