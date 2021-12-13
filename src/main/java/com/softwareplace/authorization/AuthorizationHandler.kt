package com.softwareplace.authorization

import com.softwareplace.model.UserData
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuthorizationHandler {
    fun userRole(): String
    fun authorizationSuccessfully(request: HttpServletRequest, userData: UserData)
    fun onAuthorizationFailed(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, exception: Exception)
}
