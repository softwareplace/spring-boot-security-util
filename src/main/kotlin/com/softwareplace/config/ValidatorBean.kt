package com.softwareplace.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import javax.validation.Validation
import javax.validation.Validator

@ComponentScan
class ValidatorBean {
    @Bean
    fun validator(): Validator {
        return Validation.buildDefaultValidatorFactory()
            .validator
    }
}
