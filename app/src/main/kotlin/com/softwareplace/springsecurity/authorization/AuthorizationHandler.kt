package com.softwareplace.springsecurity.authorization


import com.softwareplace.springsecurity.model.UserData
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface AuthorizationHandler {
    fun authorizationSuccessfully(request: HttpServletRequest, userData: UserData) {
        // Override this method if you need
    }

    fun onAuthorizationFailed(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        exception: Exception
    ) {
        // Override this method if you need
    }

    fun onAuthorizationFailed(request: HttpServletRequest, response: HttpServletResponse) {
        // Override this method if you need
    }
}
