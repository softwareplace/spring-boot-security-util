package com.softwareplace.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestUser(
    @field:JsonProperty("username") val username: String,
    @field:JsonProperty("password") val password: String
)
