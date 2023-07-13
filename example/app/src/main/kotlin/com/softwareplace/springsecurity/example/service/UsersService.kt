package com.softwareplace.springsecurity.example.service

import com.softwareplace.springsecurity.example.model.AppUserData
import com.softwareplace.springsecurity.example.repository.UserRepository
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.service.AuthorizationUserService
import org.springframework.stereotype.Service

@Service
class UsersService(
    private val repository: UserRepository
) : AuthorizationUserService {

    fun addUser(user: AppUserData) = repository.addUser(user)

    override fun findUser(user: RequestUser) = repository.findUserByEmail(user.username)

    override fun findUser(authToken: String) = repository.findUserToken(authToken)

    override fun loadUserByUsername(username: String) = repository.findUserToken(username)
}
