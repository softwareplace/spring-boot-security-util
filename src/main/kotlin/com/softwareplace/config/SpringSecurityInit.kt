package com.softwareplace.config

import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.security.CustomWebSecurityConfigurerAdapter
import com.softwareplace.service.AuthorizationUserService
import com.softwareplace.service.ValidatorService
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import javax.validation.Validator


interface SpringSecurityInit {

    @Bean
    fun validatorService(validator: Validator): ValidatorService {
        return ValidatorService(validator)
    }

    @Bean
    fun webSecurity(
        userDetailsService: UserDetailsService,
        authorizationUserService: AuthorizationUserService,
        authorizationHandler: AuthorizationHandler,
        applicationInfo: ApplicationInfo,
        controllerAdvice: ControllerExceptionAdvice
    ) = CustomWebSecurityConfigurerAdapter(
        userDetailsService = userDetailsService,
        authorizationUserService = authorizationUserService,
        authorizationHandler = authorizationHandler,
        applicationInfo = applicationInfo,
        controllerAdvice = controllerAdvice
    )
}
