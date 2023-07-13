package com.softwareplace.springsecurity.example.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.softwareplace.springsecurity.example.loader.JsonLoader
import com.softwareplace.springsecurity.example.model.AppUserData
import com.softwareplace.springsecurity.exception.ApiBaseException
import com.softwareplace.springsecurity.validator.SourceValidate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserRepository(
    private val sourceValidate: SourceValidate
) {
    private var inMemoryUser: MutableMap<String, AppUserData> =
        JsonLoader.loader.fromJson("user.json", typeRef())
            .associateBy { it.username }
            .toMutableMap()

    fun addUser(user: AppUserData): AppUserData {
        sourceValidate.validate(user)
        if (inMemoryUser.containsKey(user.email)) {
            throw ApiBaseException(
                message = "Unavailable email",
                status = HttpStatus.BAD_REQUEST,
                errorInfo = mapOf("duplicatedEmail" to user.email)
            )
        }

        user.apply {
            userId = (inMemoryUser.size + 1).toLong()
            createdAt = LocalDateTime.now()
        }
        inMemoryUser[user.email] = user
        return user
    }

    fun findUserByEmail(email: String): AppUserData? = inMemoryUser[email]
    fun findUserToken(token: String): AppUserData? = inMemoryUser.values.firstOrNull { token == it.authToken() }

    private fun typeRef() = object : TypeReference<List<AppUserData>>() {
    }
}
