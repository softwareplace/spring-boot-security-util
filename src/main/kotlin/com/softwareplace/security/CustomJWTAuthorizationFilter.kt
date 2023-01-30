package com.softwareplace.security

import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.authorization.JWTAuthorizationFilter
import com.softwareplace.config.ApplicationInfo
import com.softwareplace.service.AuthorizationUserService
import org.springframework.security.authentication.AuthenticationManager
import javax.servlet.http.HttpServletRequest

open class CustomJWTAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    authorizationUserService: AuthorizationUserService,
    authorizationHandler: AuthorizationHandler,
    private val applicationInfo: ApplicationInfo
) : JWTAuthorizationFilter(authenticationManager, authorizationUserService, authorizationHandler, applicationInfo) {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return openUrl(request.requestURI)
    }

    open fun openUrl(requestPath: String): Boolean {
        val split = applicationInfo.openUrl.split(",")

        for (path in split) {
            if (requestPath.contains(path)) {
                return true
            }
        }
        return false
    }
}
