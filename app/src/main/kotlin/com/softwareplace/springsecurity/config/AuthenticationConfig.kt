package com.softwareplace.springsecurity.config

import com.softwareplace.springsecurity.encrypt.Encrypt
import com.softwareplace.springsecurity.service.AuthorizationUserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
class AuthenticationConfig(
    authenticationManagerBuilder: AuthenticationManagerBuilder,
    authorizationUserService: AuthorizationUserService
) {
    init {
        authenticationManagerBuilder
            .userDetailsService(authorizationUserService)
            .passwordEncoder(Encrypt.PASSWORD_ENCODER)
    }
}
