package com.softwareplace.springsecurity.security.adapter

import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.config.ApplicationInfo
import com.softwareplace.springsecurity.config.ControllerExceptionAdvice
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

open class SecurityFilterChainConfig(
    private val decoder: JwtDecoder,
    private val applicationInfo: ApplicationInfo,
    private val authorizationHandler: AuthorizationHandler,
    private val controllerAdvice: ControllerExceptionAdvice,
    private val authenticationFilter: AbstractAuthenticationProcessingFilter,
    private val jwtAuthorizationFilter: OncePerRequestFilter
) {

    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf(::csrf)
            .cors(::corsConfiguration)
            .authorizeHttpRequests(::authorizeHttpRequest)
            .exceptionHandling(::exceptionHandler)
            .oauth2ResourceServer(::oauth2ResourceServer)
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(authenticationFilter, BasicAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthorizationFilter, AnonymousAuthenticationFilter::class.java)
            .sessionManagement(::sessionManagement)
            .build()
    }

    open fun oauth2ResourceServer(conf: OAuth2ResourceServerConfigurer<HttpSecurity>) {
        conf.jwt { jwt -> jwt.decoder(authorizationHandler.jwtDecoder() ?: decoder) }
    }

    open fun csrf(csrf: CsrfConfigurer<HttpSecurity>) {
        csrf.disable()
    }

    open fun sessionManagement(session: SessionManagementConfigurer<HttpSecurity>) {
        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    }

    open fun corsConfiguration(cors: CorsConfigurer<HttpSecurity>) {
        cors.apply { }
    }

    open fun authorizeHttpRequest(authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        authorize
            .requestMatchers(*applicationInfo.openUrl.toTypedArray())
            .permitAll()
            .anyRequest()
            .fullyAuthenticated()
    }

    open fun exceptionHandler(handler: ExceptionHandlingConfigurer<HttpSecurity>) {
        handler.authenticationEntryPoint(controllerAdvice)
            .accessDeniedHandler(controllerAdvice)
    }
}
