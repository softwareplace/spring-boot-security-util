package com.softwareplace.authorization

import com.softwareplace.authorization.ResponseRegister.register
import com.softwareplace.model.UserData
import com.softwareplace.service.AuthorizationUserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import org.springframework.http.HttpHeaders
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class JWTAuthorizationFilter(
    authenticationManager: AuthenticationManager?,
    private val authorizationUserService: AuthorizationUserService,
    private val authorizationHandler: AuthorizationHandler
) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            SecurityContextHolder.getContext().authentication = getUsernamePasswordAuthenticationToken(request)
            chain.doFilter(request, response)
        } catch (exception: Exception) {
            when (exception) {
                is Unauthorized,
                is SignatureException,
                is AccessDeniedException,
                is MalformedJwtException -> {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    register(response)
                }
                else -> register(response, ERROR_RESPONSE_MESSAGE, HttpServletResponse.SC_BAD_REQUEST, HashMap())
            }

            authorizationHandler.onAuthorizationFailed(request, response, chain, exception)
        }
    }

    private fun getUsernamePasswordAuthenticationToken(request: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val authToken = getAuthToken(request)
        val userData = authorizationUserService.userData(authToken)

        userData?.run {
            return getAuthenticationToken(request, this)
        }

        throw AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE)
    }

    private fun getAuthenticationToken(request: HttpServletRequest, userData: UserData): UsernamePasswordAuthenticationToken {
        val authorities = userData.userRoles()
            .map { role: String -> SimpleGrantedAuthority("$ROLE$role") }

        val principal = User(userData.username, userData.username, authorities)
        authorizationHandler.authorizationSuccessfully(request, userData)
        return UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
    }

    @Throws(UnsupportedOperationException::class)
    private fun getAuthToken(request: HttpServletRequest): String {
        val requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        requestHeader?.run {
            val authorization = this
                .replace(BEARER, "")

            return Jwts.parser()
                .setSigningKey(authorizationUserService.secret())
                .parseClaimsJws(authorization)
                .body
                .subject
        }

        throw AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE)
    }

    companion object {
        const val BEARER = "Bearer "
        const val UNAUTHORIZED_ERROR_MESSAGE = "Access was not authorized on this request."
        const val ROLE = "ROLE_"
        const val ERROR_RESPONSE_MESSAGE = "The request could not be completed."
    }
}
