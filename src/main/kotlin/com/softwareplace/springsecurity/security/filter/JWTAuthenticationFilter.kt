package com.softwareplace.springsecurity.security.filter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.authorization.JWTSystem
import com.softwareplace.springsecurity.authorization.JWTSystem.Companion.JWT_KEY
import com.softwareplace.springsecurity.authorization.ResponseRegister
import com.softwareplace.springsecurity.encrypt.Encrypt
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.model.UserData
import com.softwareplace.springsecurity.service.AuthorizationUserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.event.Level
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

class JWTAuthenticationFilter(
    private val authorizationUserService: AuthorizationUserService,
    private val authorizationHandler: AuthorizationHandler,
    private val jwtSystem: JWTSystem,
    private val authenticationManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher(AUTHORIZATION_PATH, METHOD_POST),
    authenticationManager
) {

    override fun attemptAuthentication(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): Authentication? {
        try {
            return authenticationHandler(httpServletRequest, httpServletResponse)
        } catch (ex: Exception) {
            ResponseRegister.register(httpServletRequest, httpServletResponse, ex).level(Level.ERROR)
                .printStackTrackerEnable()
                .error(ex)
                .run()
        }
        authorizationHandler.onAuthorizationFailed(httpServletRequest, httpServletResponse)
        return null
    }

    private fun authenticationHandler(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): Authentication? {
        buildUserRequest(httpServletRequest)?.let { requestUser ->
            authorizationUserService.findUser(requestUser)?.let { userData ->
                val encrypt = Encrypt(requestUser.password)
                if (encrypt.isValidPassword(userData.password)) {
                    return authorizationBuild(httpServletRequest, userData, requestUser)
                }
            }
        }
        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        ResponseRegister.register(httpServletRequest, httpServletResponse).level(Level.ERROR).run()
        return null
    }

    private fun authorizationBuild(
        httpServletRequest: HttpServletRequest,
        userData: UserData,
        requestUser: RequestUser
    ): Authentication {
        val claims = authorizationUserService.claims(httpServletRequest, userData)
        httpServletRequest.setAttribute(JWTAuthorizationFilter.USER_SESSION_DATA, userData)
        httpServletRequest.setAttribute(
            ACCESS_TOKEN,
            jwtSystem.jwtGenerate(claims, userData.authToken())
        )
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userData.authToken(),
                requestUser.password
            )
        )
    }

    private fun buildUserRequest(httpServletRequest: HttpServletRequest): RequestUser? {
        val objectMapper = ObjectMapper()
        val requestValue =
            objectMapper.readValue(httpServletRequest.inputStream, object : TypeReference<Map<String, String>>() {})

        requestValue[USERNAME]?.let { username ->
            requestValue[PASSWORD]?.let { password ->
                return RequestUser(username = username, password = password)
            }
        }
        return null
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val params: MutableMap<String, Any> = HashMap()
        params[JWT_KEY] = request.getAttribute(ACCESS_TOKEN)
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
        const val SUCCESS = "success"
        const val AUTHORIZATION_SUCCESSFUL = "Authorization successful."
        const val AUTHORIZATION_PATH = "/authorization"
        const val METHOD_POST = "POST"
        const val USERNAME = "username"
        const val PASSWORD = "password"
    }
}
