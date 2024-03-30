package com.softwareplace.springsecurity.example.service

import com.softwareplace.springsecurity.example.model.AppUserData
import com.softwareplace.springsecurity.example.repository.UserRepository
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.model.UserData
import com.softwareplace.springsecurity.service.AuthorizationUserService
import com.softwareplace.springsecurity.service.JwtService.Companion.SCOPES
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class UsersService(
    private val repository: UserRepository
) : AuthorizationUserService {

    override fun claims(httpServletRequest: HttpServletRequest, userData: UserData): Map<String, List<Any>> {
        return mapOf(SCOPES to listOf("user-data-access:read"))
    }

    fun addUser(user: AppUserData) = repository.save(user)

    override fun findUser(user: RequestUser) = repository.findByEmail(user.username)

    override fun findUser(authToken: String) = repository.findByToken(authToken)

    override fun loadUserByUsername(username: String) = repository.findByToken(username)
}
