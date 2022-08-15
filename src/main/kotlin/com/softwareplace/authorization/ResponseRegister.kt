package com.softwareplace.authorization

import br.com.softwareplace.json.logger.log.JsonLog
import br.com.softwareplace.json.logger.log.loggerk
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.Level
import java.io.IOException
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object ResponseRegister {

    @Throws(IOException::class)
    fun register(request: HttpServletRequest, response: HttpServletResponse) {
        if (response.status == HttpServletResponse.SC_UNAUTHORIZED) {
            register(request, response, "Access denied!", response.status, HashMap())
        }
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

        JsonLog(loggerk)
                .message(responseMessage)
                .add("status", status)
                .add("service", request.requestURI)
                .add("date", LocalDateTime.now())
                .add("customProperties", responseParams)
                .run(Level.WARN)
    }
}
