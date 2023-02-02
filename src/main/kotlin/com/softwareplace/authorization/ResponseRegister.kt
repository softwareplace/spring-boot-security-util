package com.softwareplace.authorization

import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.authorization.JWTAuthenticationFilter.Companion.JWT
import com.softwareplace.json.logger.log.JsonLog
import com.softwareplace.json.logger.log.logger
import org.slf4j.event.Level
import java.io.IOException
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object ResponseRegister {

    @Throws(IOException::class)
    fun register(request: HttpServletRequest, response: HttpServletResponse) {
        register(request, response, "Access denied!", response.status, HashMap())
    }

    @JvmStatic
    @Throws(IOException::class)
    fun register(request: HttpServletRequest, response: HttpServletResponse, message: String?, status: Int, params: Map<String, Any>) {
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

        val logParams = responseParams.filter { (k, _) -> k != JWT }

        JsonLog(logger())
            .message(responseMessage)
            .add("status", status)
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .add("customProperties", logParams)
            .run(Level.ERROR)
    }
}
