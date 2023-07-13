package com.softwareplace.springsecurity.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security")
class ApplicationInfo {

    /**
     * Jwt generator secret key
     * */
    lateinit var securitySecret: String

    /**
     * List of URLs that are open to access without authentication.
     * ## Example:
     * - /swagger-uit/<a>**</a>
     * */
    var openUrl: List<String> = emptyList()

    /**
     * List of allowed headers in CORS requests.
     * ## Default:
     * - Access-Control-Allow-Headers
     * - Authorization
     * - Content-Type
     * - authentication
     * */
    var allowedHeaders: List<String> = listOf(
        "Access-Control-Allow-Headers",
        "Authorization",
        "Content-Type",
        "authentication"
    )

    /**
     * List of allowed headers in CORS requests.
     * ## Default:
     * - GET
     * - POST
     * - PUT
     * - PATCH
     * - DELETE
     * - OPTIONS
     * */
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")

    /**
     * List of allowed origins in CORS requests.
     * - Default: <a>*</a>
     * */
    var allowedOrigins: List<String> = listOf("*")

    /**
     * CORS configuration pattern.
     * - Default: /<a>**</a>
     * */
    var corsConfigPattern: String = "/**"

    /**
     * Expiration time for JWT tokens. If not set, the generated jwt expires in 15 minutes.
     * - Default: 900000
     * */
    var jwtExpirationTime: Long = 900000L

    /**
     * When the [ControllerExceptionAdvice] handler some error,
     * with [stackTraceLogEnable] = true, than [Throwable.printStackTrace] will bell called
     * - Default: true
     * */
    var stackTraceLogEnable: Boolean = true
}

