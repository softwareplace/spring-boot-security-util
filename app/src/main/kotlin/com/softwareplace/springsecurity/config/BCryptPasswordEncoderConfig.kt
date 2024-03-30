package com.softwareplace.springsecurity.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BCryptPasswordEncoderConfig(
    private val applicationInfo: ApplicationInfo
) {

    @Bean
    fun bCryptPasswordEncoderConfig(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder(applicationInfo.encryptStrength)
    }
}
