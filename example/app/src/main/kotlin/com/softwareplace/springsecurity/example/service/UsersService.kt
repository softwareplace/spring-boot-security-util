package com.softwareplace.springsecurity.example.service

import com.softwareplace.springsecurity.encrypt.Encrypt
import com.softwareplace.springsecurity.example.model.AppUserData
import com.softwareplace.springsecurity.example.repository.UserRepository
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.service.AuthorizationUserService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsersService(
    private val repository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : AuthorizationUserService {

    fun addUser(user: AppUserData): AppUserData {
        val userData = user.apply {
            val encrypt = Encrypt(pass, bCryptPasswordEncoder)
            userPasswordValidate = pass
            token = encrypt.authToken
            pass = encrypt.encodedPassword
        }
        return repository.save(userData)
    }

    override fun findUser(user: RequestUser) = repository.findByEmail(user.username)

    override fun findUser(authToken: String) = repository.findByToken(authToken)

    override fun loadUserByUsername(username: String) = repository.findByToken(username)
}
