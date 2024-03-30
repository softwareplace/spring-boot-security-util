package com.softwareplace.springsecurity.security.adapter

import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.config.ApplicationInfo
import com.softwareplace.springsecurity.config.ControllerExceptionAdvice
import com.softwareplace.springsecurity.security.filter.JWTAuthenticationFilter
import com.softwareplace.springsecurity.security.filter.JWTAuthorizationFilter
import com.softwareplace.springsecurity.service.AuthorizationUserService
import com.softwareplace.springsecurity.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import java.util.*


@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val decoder: JwtDecoder,
    private val jwtService: JwtService,
    private val applicationInfo: ApplicationInfo,
    private val authorizationHandler: AuthorizationHandler,
    private val controllerAdvice: ControllerExceptionAdvice,
    private val jwtAuthorizationFilter: JWTAuthorizationFilter,
    private val authorizationUserService: AuthorizationUserService,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val securityFilterChainConfig: Optional<SecurityFilterChainConfig>
) {

    private val authenticationFilter: JWTAuthenticationFilter by lazy {
        JWTAuthenticationFilter(
            authorizationUserService,
            authorizationHandler,
            jwtService,
            authenticationConfiguration.authenticationManager
        )
    }

    private val chainConfig: SecurityFilterChainConfig by lazy {
        securityFilterChainConfig.orElse(
            SecurityFilterChainConfig(
                decoder,
                applicationInfo,
                authorizationHandler,
                controllerAdvice,
                authenticationFilter,
                jwtAuthorizationFilter
            )
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return chainConfig.securityFilterChain(http)
    }
}
