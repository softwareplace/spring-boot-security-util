package com.softwareplace.springsecurity.service

import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.model.UserData
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

interface AuthorizationUserService : UserDetailsService {

    fun findUser(user: RequestUser): UserData?
    fun findUser(authToken: String): UserData?

    fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        // ignored
    }

    fun claims(httpServletRequest: HttpServletRequest, userData: UserData): Map<String, List<Any>> {
        return HashMap()
    }

    fun authenticate(
        request: HttpServletRequest,
        defaultHandler: (request: HttpServletRequest) -> UsernamePasswordAuthenticationToken
    ): UsernamePasswordAuthenticationToken {
        return defaultHandler.invoke(request)
    }
}
