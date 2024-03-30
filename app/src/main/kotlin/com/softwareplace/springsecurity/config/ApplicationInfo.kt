package com.softwareplace.springsecurity.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@ConfigurationProperties(prefix = "spring.security")
class ApplicationInfo {

    /**
     * [issuer] the issuer identifier
     */
    lateinit var issuer: String

    /**
     * [pubKey] the public jwt generate key file path
     */
    lateinit var pubKey: RSAPublicKey

    /**
     * [privateKey] the private jwt generate key file path
     */
    lateinit var privateKey: RSAPrivateKey

    /**
     * [encryptStrength] the log rounds to use, between 4 and 31 required by [BCryptPasswordEncoder].
     *
     * Uses strength as 6 if note set.
     * */
    var encryptStrength: Int = 6

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

