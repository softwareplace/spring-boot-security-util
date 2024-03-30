package com.softwareplace.springsecurity.example.controller

import com.softwareplace.springsecurity.example.model.UserMapper
import com.softwareplace.springsecurity.example.rest.controller.UsersController
import com.softwareplace.springsecurity.example.rest.model.UserDetailRest
import com.softwareplace.springsecurity.example.rest.model.UserRest
import com.softwareplace.springsecurity.example.service.UsersService
import com.softwareplace.springsecurity.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UsersControllerImpl(
    private val service: UsersService,
    private val mapper: UserMapper,
    private val jwtService: JwtService
) : UsersController {

    override suspend fun createAccount(userRest: UserRest): ResponseEntity<UserDetailRest> {
        return mapper.parse(userRest)
            .run(service::addUser)
            .run(mapper::parse)
            .run { ResponseEntity(this, HttpStatus.CREATED) }
    }

    override suspend fun getUserDetail(authorization: String): ResponseEntity<UserDetailRest> {
        return jwtService.getJwt(authorization)
            .run { this.subject }
            ?.run(service::findUser)
            ?.run(mapper::parse)!!
            .run { ResponseEntity.ok(this) }
    }
}
