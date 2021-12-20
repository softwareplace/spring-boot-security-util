package com.softwareplace

import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.config.ControllerExceptionAdvice
import com.softwareplace.model.RequestUser
import com.softwareplace.model.UserData
import com.softwareplace.service.AuthorizationUserService
import com.softwareplace.service.ValidatorService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Validator

@SpringBootApplication
open class App {

    @Bean
    open fun userDetailsService() = UserDetailsService { null }

    @Bean
    open fun authorizationUserService() = object : AuthorizationUserService {
        override fun findUser(user: RequestUser): UserData? {
            return null
        }

        override fun findUser(authToken: String): UserData? {
            return null
        }
    }

    @Bean
    open fun authorizationHandler() = object : AuthorizationHandler {
        override fun userRole() = "USER"

        override fun authorizationSuccessfully(request: HttpServletRequest, userData: UserData) {
        }

        override fun onAuthorizationFailed(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, exception: Exception) {
        }
    }

    @Bean
    open fun controllerAdvice() = object : ControllerExceptionAdvice() {}

    @Bean
    open fun validatorService(validator: Validator) = ValidatorService(validator)
}
