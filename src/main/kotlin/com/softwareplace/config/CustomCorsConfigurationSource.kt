package com.softwareplace.config

import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


interface CustomCorsConfigurationSource {

    @Bean
    fun corsConfigurationSource(applicationInfo: ApplicationInfo): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedMethods = applicationInfo.allowedMethods
            allowedHeaders = applicationInfo.allowedHeaders
            allowedOrigins = applicationInfo.allowedOrigins
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration(applicationInfo.corsConfigPattern, configuration)
        return source
    }
}
