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
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jwtService: JwtService,
    private val applicationInfo: ApplicationInfo,
    private val authorizationHandler: AuthorizationHandler,
    private val controllerAdvice: ControllerExceptionAdvice,
    private val jwtAuthorizationFilter: JWTAuthorizationFilter,
    private val authorizationUserService: AuthorizationUserService,
    private val authenticationConfiguration: AuthenticationConfiguration,
) {

    private val authenticationFilter: JWTAuthenticationFilter by lazy {
        JWTAuthenticationFilter(
            authorizationUserService,
            authorizationHandler,
            jwtService,
            authenticationConfiguration.authenticationManager
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf(this::csrf)
            .cors(this::corsConfiguration)
            .authorizeHttpRequests(this::authorizeHttpRequest)
            .exceptionHandling(this::exceptionHandler)
            .addFilterBefore(authenticationFilter, BasicAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthorizationFilter, AnonymousAuthenticationFilter::class.java)
            .sessionManagement(this::sessionManagement)
            .build()
    }

    private fun csrf(csrf: CsrfConfigurer<HttpSecurity>) {
        csrf.disable()
    }

    private fun sessionManagement(session: SessionManagementConfigurer<HttpSecurity>) {
        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    }

    private fun corsConfiguration(cors: CorsConfigurer<HttpSecurity>) {
        cors.apply { }
    }

    private fun authorizeHttpRequest(authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        authorize
            .requestMatchers(*applicationInfo.openUrl.toTypedArray())
            .permitAll()
            .anyRequest()
            .fullyAuthenticated()
    }

    private fun exceptionHandler(handler: ExceptionHandlingConfigurer<HttpSecurity>) {
        handler.authenticationEntryPoint(controllerAdvice)
            .accessDeniedHandler(controllerAdvice)
    }
}
