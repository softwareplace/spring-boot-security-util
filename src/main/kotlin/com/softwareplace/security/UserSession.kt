package com.softwareplace.security

import com.softwareplace.authorization.JWTAuthorizationFilter.Companion.USER_SESSION_DATA
import com.softwareplace.model.UserData
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest


inline fun <reified T : UserData> getSessionUser(): T? {
    val servletRequest: HttpServletRequest = (RequestContextHolder.currentRequestAttributes()
            as ServletRequestAttributes).request
    return servletRequest.getAttribute(USER_SESSION_DATA) as T?
}



