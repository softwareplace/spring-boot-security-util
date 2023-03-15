package com.softwareplace

import com.softwareplace.authorization.AuthorizationHandler
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
    }

    @Bean
    fun authorizationHandler() = object : AuthorizationHandler {

        override fun userConfig(): List<Pair<String, List<String>>> {
            return listOf(
                "/**" to listOf("USER"),
            )
        }

        override fun authorizationSuccessfully(request: HttpServletRequest, userData: UserData) {
        }

        override fun onAuthorizationFailed(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, exception: Exception) {
        }

        override fun onAuthorizationFailed(request: HttpServletRequest, response: HttpServletResponse) {

        }
    }

//    @Bean
//    fun controllerAdvice() = object : ControllerExceptionAdvice(getObjectMapper()) {}

    @Bean
    fun validatorService(validator: Validator) = ValidatorService(validator)
}
