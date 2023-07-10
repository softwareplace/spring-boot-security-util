package com.softwareplace.springsecurity.security.filter

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.authorization.JWTSystem
import com.softwareplace.springsecurity.authorization.ResponseRegister
import com.softwareplace.springsecurity.config.ApplicationInfo
import com.softwareplace.springsecurity.exception.UnauthorizedAccessExceptionApi
import com.softwareplace.springsecurity.extension.asPathRegex
import com.softwareplace.springsecurity.model.toAuthorizationUser
import com.softwareplace.springsecurity.service.AuthorizationUserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.event.Level
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.filter.OncePerRequestFilter
import java.security.SignatureException

@Component
class JWTAuthorizationFilter(
    private val authorizationUserService: AuthorizationUserService,
    private val authorizationHandler: AuthorizationHandler,
    private val applicationInfo: ApplicationInfo,
    private val jwtSystem: JWTSystem
) : OncePerRequestFilter() {

    fun isOpenUrl(requestPath: String): Boolean {
        return applicationInfo.openUrl.any { pathPattern -> requestPath.matches(pathPattern.asPathRegex()) }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return isOpenUrl(request.servletPath)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            val passwordAuthenticationToken = authorizationUserService.authenticate(
                request = request,
                defaultHandler = this::getUsernamePasswordAuthenticationToken
            )
            SecurityContextHolder.getContext().authentication = passwordAuthenticationToken
            chain.doFilter(request, response)
        } catch (exception: Exception) {
            when (exception) {
                is HttpClientErrorException.Unauthorized,
                is SignatureException,
                is AccessDeniedException,
                is UnauthorizedAccessExceptionApi -> {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    ResponseRegister.register(request, response, exception).level(Level.ERROR).run()
                }
                is JWTVerificationException -> {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    ResponseRegister.register(request, response).level(Level.ERROR).run()
                }

                else -> ResponseRegister.register(
                    request,
                    response,
                    ERROR_RESPONSE_MESSAGE,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    HashMap()
                )
                    .level(Level.ERROR).run()
            }

            authorizationHandler.onAuthorizationFailed(request, response, chain, exception)
        }
    }

    private fun getUsernamePasswordAuthenticationToken(request: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val decodedJWT: DecodedJWT? = jwtSystem.getJwt(request)

        val userData = when (decodedJWT != null) {
            true -> authorizationUserService.findUser(decodedJWT.subject)
            false -> null
        }

        userData?.run {
            authorizationHandler.authorizationSuccessfully(request, this)
            request.setAttribute(USER_SESSION_DATA, this)
            return UsernamePasswordAuthenticationToken(
                toAuthorizationUser, null,
                toAuthorizationUser.authorities
            )
        }

        throw AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE)
    }

    companion object {
        const val BEARER = "Bearer "
        const val BASIC = "Basic "
        const val UNAUTHORIZED_ERROR_MESSAGE = "Access was not authorized on this request."
        const val ROLE = "ROLE_"
        const val ERROR_RESPONSE_MESSAGE = "Access denied."
        const val USER_SESSION_DATA = "USER_SESSION_DATA"
    }
}
