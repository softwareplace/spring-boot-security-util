package com.softwareplace

import com.softwareplace.config.ApplicationInfo
import com.softwareplace.config.CustomCorsConfiguration
import com.softwareplace.security.adapter.CustomWebSecurityConfigurerAdapter
import com.softwareplace.service.ValidatorService
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.validation.Validator

@Configuration
@ImportAutoConfiguration(
    classes = [
        CustomCorsConfiguration::class,
        CustomWebSecurityConfigurerAdapter::class,
    ]
)
@EnableConfigurationProperties(value = [ApplicationInfo::class])
class SpringSecurityInit {

    @Bean
    fun validatorService(validator: Validator): ValidatorService {
        return ValidatorService(validator)
    }
}
