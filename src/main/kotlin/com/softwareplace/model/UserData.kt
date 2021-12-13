package com.softwareplace.model

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
