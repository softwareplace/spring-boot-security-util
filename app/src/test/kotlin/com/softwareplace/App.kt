package com.softwareplace

import com.softwareplace.springsecurity.SpringSecurityInit
import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.model.UserData
import com.softwareplace.springsecurity.service.AuthorizationUserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService


@SpringBootApplication
@ComponentScan(basePackageClasses = [SpringSecurityInit::class])
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
        override fun authorizationSuccessfully(request: HttpServletRequest, userData: UserData) {
            TODO("Not yet implemented")
        }

        override fun onAuthorizationFailed(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain,
            exception: Exception
        ) {
            TODO("Not yet implemented")
        }

        override fun onAuthorizationFailed(
            request: HttpServletRequest,
            response: HttpServletResponse
        ) {
            TODO("Not yet implemented")
        }


    }

//    @Bean
//    @Primary
//    fun controllerAdvice() = ControllerExceptionAdvice(getObjectMapper(), applicationInfo = ApplicationInfo())
}
