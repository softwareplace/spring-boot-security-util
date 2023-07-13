package com.softwareplace.springsecurity.example.model;

import com.softwareplace.springsecurity.example.rest.model.UserDetailRest
import com.softwareplace.springsecurity.example.rest.model.UserRest
import org.mapstruct.Mapper;
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {

    @Mapping(source = "password", target = "pass")
    fun parse(input: UserRest): AppUserData

    fun parse(input: AppUserData): UserDetailRest
}
