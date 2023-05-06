package com.softwareplace

import com.softwareplace.jsonlogger.mapper.getObjectMapper
import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.config.ControllerExceptionAdvice
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.model.UserData
import com.softwareplace.springsecurity.service.AuthorizationUserService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService


@SpringBootApplication
class App {

    @Bean
    fun userDetailsService() = UserDetailsService { null }

    @Bean
    fun authorizationUserService() = object : AuthorizationUserService {
        override fun findUser(user: RequestUser): UserData? {
            return null
        }

        override fun findUser(authToken: String): UserData? {
            return null
        }

        override fun loadUserByUsername(username: String?): UserDetails {
            TODO("Not yet implemented")
        }
    }

    @Bean
    fun authorizationHandler() = object : AuthorizationHandler {
        override fun authorizationSuccessfully(request: jakarta.servlet.http.HttpServletRequest, userData: UserData) {
            TODO("Not yet implemented")
        }

        override fun onAuthorizationFailed(
            request: jakarta.servlet.http.HttpServletRequest,
            response: jakarta.servlet.http.HttpServletResponse,
            chain: jakarta.servlet.FilterChain,
            exception: Exception
        ) {
            TODO("Not yet implemented")
        }

        override fun onAuthorizationFailed(
            request: jakarta.servlet.http.HttpServletRequest,
            response: jakarta.servlet.http.HttpServletResponse
        ) {
            TODO("Not yet implemented")
        }


    }

    @Bean
    @Primary
    fun controllerAdvice() = ControllerExceptionAdvice(getObjectMapper())
}
