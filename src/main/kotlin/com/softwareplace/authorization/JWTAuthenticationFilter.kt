package com.softwareplace.authorization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.softwareplace.config.ApplicationInfo
import com.softwareplace.encrypt.Encrypt
import com.softwareplace.model.RequestUser
import com.softwareplace.service.AuthorizationUserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
    private val authorizationUserService: AuthorizationUserService,
    private val authManager: AuthenticationManager,
    private val applicationInfo: ApplicationInfo
) : CustomAuthenticationProcessingFilter() {

    @Throws(AuthenticationException::class, IOException::class)
    override fun attemptAuthentication(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): Authentication? {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(KotlinModule.Builder().build())

        val requestUser = objectMapper.readValue(httpServletRequest.inputStream, RequestUser::class.java)
        val userData = authorizationUserService.findUser(requestUser)
        if (userData != null) {
            val encrypt = Encrypt(requestUser.password)
            if (encrypt.isValidPassword(userData.password)) {
                val claims = authorizationUserService.claims(httpServletRequest, userData)
                val jwtGenerate = JWTGenerate(applicationInfo)
                httpServletRequest.setAttribute(JWTAuthorizationFilter.USER_SESSION_DATA, userData)
                httpServletRequest.setAttribute(ACCESS_TOKEN, jwtGenerate.tokenGenerate(claims, userData.authToken()))
                return this.authManager.authenticate(UsernamePasswordAuthenticationToken(userData.authToken(), requestUser.password))
            }
        }
        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        ResponseRegister.register(httpServletResponse)
        return null
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val params: MutableMap<String, Any> = HashMap()
        params[JWT] = request.getAttribute(ACCESS_TOKEN)
        params[SUCCESS] = true
        ResponseRegister.register(response, AUTHORIZATION_SUCCESSFUL, 200, params)
        authorizationUserService.successfulAuthentication(request, response, chain, authResult)
    }

    companion object {
        private const val ACCESS_TOKEN = "accessToken"
        const val JWT = "jwt"
        const val SUCCESS = "success"
        const val AUTHORIZATION_SUCCESSFUL = "Authorization successful."
    }
}
