package com.softwareplace.springsecurity.example.model

import com.softwareplace.springsecurity.encrypt.Encrypt
import com.softwareplace.springsecurity.model.UserData
import com.softwareplace.springsecurity.validator.annotation.ValidEmail
import com.softwareplace.springsecurity.validator.annotation.ValidPassword
import java.time.LocalDateTime


data class AppUserData(
    var userId: Long = 0,
    val name: String,
    @ValidEmail
    val email: String,
    var createdAt: LocalDateTime? = null,
    @ValidPassword
    private val pass: String,
    private val token: String? = null
) : UserData {

    private val encrypt = Encrypt(pass)

    override fun getPassword(): String = encrypt.encodedPassword

    override fun getUsername(): String = email

    override fun authToken(): String = encrypt.authToken
}
