package com.softwareplace.security

import com.softwareplace.model.UserData
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

const val ACCESS_USER = "ACCESS_USER"


fun setSessionUser(): UserData? {
    val servletRequest: HttpServletRequest = (RequestContextHolder.currentRequestAttributes()
            as ServletRequestAttributes).request
    return servletRequest.userPrincipal as UserData
}



