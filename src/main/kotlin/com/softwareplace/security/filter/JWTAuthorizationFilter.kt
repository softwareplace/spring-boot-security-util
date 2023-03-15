package com.softwareplace.security.filter

import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.authorization.ResponseRegister
import com.softwareplace.config.ApplicationInfo
import com.softwareplace.extension.asPathRegex
import com.softwareplace.model.toAuthorizationUser
import com.softwareplace.service.AuthorizationUserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import org.slf4j.event.Level
import org.springframework.http.HttpHeaders
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(
    private val authorizationUserService: AuthorizationUserService,
    private val authorizationHandler: AuthorizationHandler,
    private val applicationInfo: ApplicationInfo
) : OncePerRequestFilter() {

    private fun isOpenUrl(requestPath: String): Boolean {
        return applicationInfo.openUrl.any { pathPattern -> requestPath.matches(pathPattern.asPathRegex()) }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return isOpenUrl(request.servletPath)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            SecurityContextHolder.getContext().authentication = getUsernamePasswordAuthenticationToken(request)
            chain.doFilter(request, response)
        } catch (exception: Exception) {
            when (exception) {
                is HttpClientErrorException.Unauthorized,
                is SignatureException,
                is AccessDeniedException,
                is MalformedJwtException -> {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    ResponseRegister.register(request, response).level(Level.ERROR).run()
                }

                else -> ResponseRegister.register(
                    request,
                    response,
                    ERROR_RESPONSE_MESSAGE,
                    HttpServletResponse.SC_BAD_REQUEST,
                    HashMap()
                )
                    .level(Level.ERROR).run()
            }

            authorizationHandler.onAuthorizationFailed(request, response, chain, exception)
        }
    }

    private fun getUsernamePasswordAuthenticationToken(request: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val authToken = getAuthToken(request)
        val userData = authorizationUserService.findUser(authToken)

        userData?.run {
            authorizationHandler.authorizationSuccessfully(request, this)
            request.setAttribute(USER_SESSION_DATA, this)
            return UsernamePasswordAuthenticationToken(this.toAuthorizationUser, null, this.toAuthorizationUser.authorities)
        }

        throw AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE)
    }

    @Throws(UnsupportedOperationException::class)
    fun getAuthToken(request: HttpServletRequest): String {
        val requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        requestHeader?.run {
            if (this.startsWith(BASIC)) {
                val header = replace("Basic ", "")
                return String(Base64.getDecoder().decode(header))
            }

            val authorization = replace(BEARER, "")

            return Jwts.parser()
                .setSigningKey(applicationInfo.securitySecret)
                .parseClaimsJws(authorization)
                .body
                .subject
        }

        throw AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE)
    }

    companion object {
        const val BEARER = "Bearer "
        const val BASIC = "Basic "
        const val UNAUTHORIZED_ERROR_MESSAGE = "Access was not authorized on this request."
        const val ROLE = "ROLE_"
        const val ERROR_RESPONSE_MESSAGE = "The request could not be completed."
        const val USER_SESSION_DATA = "USER_SESSION_DATA"
    }
}
