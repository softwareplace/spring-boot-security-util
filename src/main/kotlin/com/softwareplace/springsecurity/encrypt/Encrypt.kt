package com.softwareplace.springsecurity.encrypt

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@Suppress("JoinDeclarationAndAssignment", "MemberVisibilityCanBePrivate")
class Encrypt(private val password: String) {

    val encodedPassword: String
    val token: String
    val authToken: String
    val salt: String

    init {
        encodedPassword = PASSWORD_ENCODER.encode(password)
        token = PASSWORD_ENCODER.encode(encodedPassword + mixedString())
        authToken = PASSWORD_ENCODER.encode(token)
        salt = PASSWORD_ENCODER.encode(authToken + token)
    }

    private fun mixedString(): String {
        val randomResult = Random().nextInt(100000)
        return (System.currentTimeMillis() + randomResult).toString()
    }

    fun isValidPassword(encodedPassword: String?): Boolean {
        return PASSWORD_ENCODER.matches(password, encodedPassword)
    }

    companion object {
        const val ENCODER_STRENGTH = 5
        val PASSWORD_ENCODER = BCryptPasswordEncoder(ENCODER_STRENGTH)
    }
}
