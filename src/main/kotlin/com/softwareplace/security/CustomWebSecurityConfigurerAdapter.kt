package com.softwareplace.security

import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.authorization.JWTAuthenticationFilter
import com.softwareplace.config.ApplicationInfo
import com.softwareplace.config.ControllerExceptionAdvice
import com.softwareplace.encrypt.Encrypt
import com.softwareplace.service.AuthorizationUserService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

open class CustomWebSecurityConfigurerAdapter(
    private val userDetailsService: UserDetailsService,
    private val authorizationUserService: AuthorizationUserService,
    private val authorizationHandler: AuthorizationHandler,
    private val applicationInfo: ApplicationInfo,
    private val controllerAdvice: ControllerExceptionAdvice,
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val docUrl = applicationInfo.openUrl.split(",")
        docUrl.forEach {
            http.authorizeRequests().antMatchers("/**/$it/**").permitAll()
        }

        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").hasRole(authorizationHandler.userRole()).and()
            .exceptionHandling()
            .authenticationEntryPoint(controllerAdvice)
            .accessDeniedHandler(controllerAdvice)
            .and()
            .addFilterBefore(
                JWTAuthenticationFilter(authorizationUserService, authenticationManager()),
                BasicAuthenticationFilter::class.java
            )
            .addFilterAfter(
                CustomJWTAuthorizationFilter(authenticationManager(), authorizationUserService, authorizationHandler, applicationInfo),
                BasicAuthenticationFilter::class.java
            )
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("authorization", "content-type", "x-auth-token", "authentication")
        configuration.exposedHeaders = listOf("x-auth-token")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(Encrypt.PASSWORD_ENCODER)
    }
}
