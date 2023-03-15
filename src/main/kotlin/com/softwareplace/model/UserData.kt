package com.softwareplace.model

import com.softwareplace.security.filter.JWTAuthorizationFilter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

interface UserData : UserDetails {

    fun authToken(): String

    fun userRoles(): Array<String>

    fun role(): Int {
        return Int.MIN_VALUE
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }
}

val UserData.authoritiesRoles
    get() = userRoles()
        .map { role: String -> SimpleGrantedAuthority("${JWTAuthorizationFilter.ROLE}$role") }

val UserData.toAuthorizationUser get() = User(username, password, authoritiesRoles)
