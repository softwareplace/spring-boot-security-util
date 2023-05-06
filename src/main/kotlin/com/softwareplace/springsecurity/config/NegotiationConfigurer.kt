package com.softwareplace.springsecurity.config

import com.softwareplace.springsecurity.interceptor.SecurityRequirementInterceptor
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class NegotiationConfigurer(
    private val securityRequirementInterceptor: SecurityRequirementInterceptor
) : WebMvcConfigurer {
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(securityRequirementInterceptor)
    }
}
