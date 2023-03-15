package com.softwareplace.security.filter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.authorization.JWTGenerate
import com.softwareplace.authorization.ResponseRegister
import com.softwareplace.config.ApplicationInfo
import com.softwareplace.encrypt.Encrypt
import com.softwareplace.model.RequestUser
import com.softwareplace.service.AuthorizationUserService
import org.slf4j.event.Level
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
    private val authorizationUserService: AuthorizationUserService,
    private val authManager: AuthenticationManager,
    private val authorizationHandler: AuthorizationHandler,
    private val applicationInfo: ApplicationInfo
) : CustomAuthenticationProcessingFilter() {

    override fun attemptAuthentication(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): Authentication? {
        buildUserRequest(httpServletRequest)?.let { requestUser ->
            authorizationUserService.findUser(requestUser)?.let { userData ->
                val encrypt = Encrypt(requestUser.password)
                if (encrypt.isValidPassword(userData.password)) {
                    val claims = authorizationUserService.claims(httpServletRequest, userData)
                    val jwtGenerate = JWTGenerate(applicationInfo)
                    httpServletRequest.setAttribute(JWTAuthorizationFilter.USER_SESSION_DATA, userData)
                    httpServletRequest.setAttribute(ACCESS_TOKEN, jwtGenerate.tokenGenerate(claims, userData.authToken()))
                    return this.authManager.authenticate(UsernamePasswordAuthenticationToken(userData.authToken(), requestUser.password))
                }
            }
        }

        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        ResponseRegister.register(httpServletRequest, httpServletResponse).level(Level.ERROR).run()
        authorizationHandler.onAuthorizationFailed(httpServletRequest, httpServletResponse)
        return null
    }

    private fun buildUserRequest(httpServletRequest: HttpServletRequest): RequestUser? {
        val objectMapper = ObjectMapper()
        val requestValue = objectMapper.readValue(httpServletRequest.inputStream, object : TypeReference<Map<String, String>>() {})

        requestValue["username"]?.let { username ->
            requestValue["password"]?.let { password ->
                return RequestUser(username = username, password = password)
            }
        }
        return null
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val params: MutableMap<String, Any> = HashMap()
        params[JWT] = request.getAttribute(ACCESS_TOKEN)
        params[SUCCESS] = true
        ResponseRegister.register(
            request,
            response,
            AUTHORIZATION_SUCCESSFUL,
            200,
            params
        ).level(Level.INFO).run()
        authorizationUserService.successfulAuthentication(request, response, chain, authResult)
    }

    companion object {
        private const val ACCESS_TOKEN = "accessToken"
        const val JWT = "jwt"
        const val SUCCESS = "success"
        const val AUTHORIZATION_SUCCESSFUL = "Authorization successful."
        const val AUTHORIZATION_PATH = "/authorization"
        const val METHOD_POST = "POST"
    }
}
