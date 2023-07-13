package com.softwareplace.springsecurity.authorization


import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.jsonlogger.log.JsonLog
import com.softwareplace.jsonlogger.log.kLogger
import com.softwareplace.springsecurity.authorization.JWTSystem.Companion.JWT_KEY
import com.softwareplace.springsecurity.exception.ApiBaseException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

object ResponseRegister {

    @Throws(IOException::class)
    fun register(request: HttpServletRequest, response: HttpServletResponse) =
        register(request, response, "Access denied!", response.status, HashMap())

    @Throws(IOException::class)
    fun register(request: HttpServletRequest, response: HttpServletResponse, ex: Exception) =
        when (ex is ApiBaseException) {
            true -> register(request, response, ex.message, ex.status.value(), ex.errorInfo)
            false -> register(request, response, "Access denied!", response.status, HashMap())
        }

    @JvmStatic
    @Throws(IOException::class)
    fun register(
        request: HttpServletRequest,
        response: HttpServletResponse,
        message: String?,
        status: Int,
        params: Map<String, Any>
    ): JsonLog {
        val responseParams = HashMap<String?, Any?>()
        val responseMessage = message ?: "Unexpected Error"
        responseParams["message"] = responseMessage
        responseParams["timestamp"] = Date().time
        responseParams["success"] = false
        responseParams["code"] = status
        response.status = status
        response.contentType = "application/json;charset=UTF-8"
        responseParams.putAll(params)
        ObjectMapper().writeValue(response.outputStream, responseParams)

        val logParams = responseParams.filter { (k, _) -> k != JWT_KEY }

        return JsonLog(kLogger)
            .message(responseMessage)
            .add("status", status)
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .add("customProperties", logParams)
    }
}
