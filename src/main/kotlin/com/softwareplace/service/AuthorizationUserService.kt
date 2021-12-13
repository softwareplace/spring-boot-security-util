package com.softwareplace.service

import com.softwareplace.model.RequestUser
import com.softwareplace.model.UserData
import org.springframework.security.core.Authentication
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuthorizationUserService {

    fun userData(user: RequestUser): UserData?
    fun userData(authToken: String): UserData?
    fun expirationTime(): Long
    fun secret(): String
    fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {}
    fun claims(httpServletRequest: HttpServletRequest?, userData: UserData): Map<String, Any> {
        return HashMap()
    }
}
