package com.softwareplace.springsecurity.interceptor

import com.softwareplace.springsecurity.authorization.JWTSystem
import com.softwareplace.springsecurity.exception.UnauthorizedAccessExceptionApi
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*

@Component
class SecurityRequirementInterceptor(
    private val jwtSystem: JWTSystem,
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            return checkOperation(handler, request)
        }
        return true
    }

    private fun checkOperation(
        handler: HandlerMethod,
        request: HttpServletRequest
    ): Boolean {
        val operation: Operation? = handler.getMethodAnnotation(Operation::class.java)

        if (operation != null) {
            return scopeAccessValidation(operation, request)
        }
        return true
    }

    private fun scopeAccessValidation(
        operation: Operation,
        request: HttpServletRequest
    ): Boolean {
        val requiredScopes = mutableListOf<String>()

        operation.security.forEach { requiredScopes.addAll(it.scopes) }

        if (requiredScopes.isNotEmpty()) {
            val decodedJWT = Optional.ofNullable(jwtSystem.getJwt(request))
                .orElseThrow { throwUnauthorizedAccess(requiredScopes) }

            val authorizedScopes: List<String> = jwtSystem.getScopes(decodedJWT)

            if (!requiredScopes.toTypedArray().any { it in authorizedScopes }) {
                throw throwUnauthorizedAccess(requiredScopes)
            }

        }
        return true
    }

    private fun throwUnauthorizedAccess(scopes: MutableList<String>): UnauthorizedAccessExceptionApi {
        return UnauthorizedAccessExceptionApi(
            message = "Missing access scope on this resource",
            status = HttpStatus.FORBIDDEN,
            info = mapOf("requiredScopes" to scopes)
        )
    }
}
