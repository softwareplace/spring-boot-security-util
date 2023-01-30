package com.softwareplace.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security-util")
open class ApplicationInfo {

    lateinit var name: String
    lateinit var version: String
    lateinit var openUrl: String
    lateinit var securitySecret: String
    var jwtExpirationTime: Long = 7776000000L
}
