package com.softwareplace.springsecurity.security.adapter

import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.authorization.JWTSystem
import com.softwareplace.springsecurity.config.ApplicationInfo
import com.softwareplace.springsecurity.config.ControllerExceptionAdvice
import com.softwareplace.springsecurity.security.filter.JWTAuthenticationFilter
import com.softwareplace.springsecurity.security.filter.JWTAuthorizationFilter
import com.softwareplace.springsecurity.service.AuthorizationUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Configuration
@EnableWebSecurity
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class WebSecurityConfig(
    private val jwtSystem: JWTSystem,
    private val applicationInfo: ApplicationInfo,
    private val authorizationHandler: AuthorizationHandler,
    private val controllerAdvice: ControllerExceptionAdvice,
    private val jwtAuthorizationFilter: JWTAuthorizationFilter,
    private val authorizationUserService: AuthorizationUserService,
    private val authenticationConfiguration: AuthenticationConfiguration
) {

    private val authenticationFilter: JWTAuthenticationFilter by lazy {
        JWTAuthenticationFilter(
            authorizationUserService,
            authorizationHandler,
            jwtSystem,
            authenticationConfiguration.authenticationManager
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf().disable()
            .cors().and()
//            .authorizeHttpRequests()
//            .requestMatchers(*applicationInfo.openUrl.toTypedArray())
//            .permitAll()
            .authorizeHttpRequests()
            .anyRequest()
            .permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(controllerAdvice)
            .accessDeniedHandler(controllerAdvice)
            .and()
            .addFilterBefore(authenticationFilter, BasicAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .build()
    }
}
