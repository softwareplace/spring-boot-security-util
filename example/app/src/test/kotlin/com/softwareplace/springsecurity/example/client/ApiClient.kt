package com.softwareplace.springsecurity.example.client

import com.softwareplace.springsecurity.example.rest.model.AuthorizationRest
import com.softwareplace.springsecurity.example.rest.model.UserDetailRest
import com.softwareplace.springsecurity.example.rest.model.UserRest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    data class AuthorizationRequest(
        val username: String,
        val password: String
    )

    @POST("/api/spring-security-example/authorization")
    fun authorization(
        @Body request: AuthorizationRequest,
    ): Call<AuthorizationRest>

    @POST("/api/spring-security-example/users/create")
    fun create(
        @Body user: UserRest,
    ): Call<UserDetailRest>

    @GET("/api/spring-security-example/users")
    fun get(
        @Header(value = "authorization") authorization: String,
    ): Call<UserDetailRest>

    @GET("/api/spring-security-example/v3/api-docs")
    fun v3ApiDoc(): Call<Map<String, Any>>

    @GET("/api/spring-security-example/swagger-ui/index.html")
    fun swaggerDoc(): Call<ResponseBody>
}
