package com.softwareplace.service

import com.softwareplace.model.RequestUser
import com.softwareplace.model.UserData
import org.springframework.security.core.Authentication
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuthorizationUserService {

    fun findUser(user: RequestUser): UserData?
    fun findUser(authToken: String): UserData?
    fun expirationTime(): Long
    fun authorizationSecrete(): String
    fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {}
    fun claims(httpServletRequest: HttpServletRequest, userData: UserData): Map<String, Any> {
        return HashMap()
    }
}
