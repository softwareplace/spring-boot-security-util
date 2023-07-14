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

    fun addUser(user: AppUserData) = repository.save(user)

    override fun findUser(user: RequestUser) = repository.findByEmail(user.username)

    override fun findUser(authToken: String) = repository.findByToken(authToken)

    override fun loadUserByUsername(username: String) = repository.findByToken(username)
}
