package com.softwareplace.authorization

import br.com.softwareplace.json.logger.log.JsonLog
import br.com.softwareplace.json.logger.log.loggerk
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.Level
import java.io.IOException
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletResponse

object ResponseRegister {

    @Throws(IOException::class)
    fun register(response: HttpServletResponse) {
        if (response.status == HttpServletResponse.SC_UNAUTHORIZED) {
            register(response, "Access denied!", response.status, HashMap())
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun register(response: HttpServletResponse, message: String?, status: Int, params: Map<String, Any>) {
        val responseParams = HashMap<String?, Any?>()
        responseParams["message"] = message ?: "Unexpected Error"
        responseParams["timestamp"] = Date().time
        responseParams["success"] = false
        responseParams["code"] = status
        response.status = status
        response.contentType = "application/json;charset=UTF-8"
        responseParams.putAll(params)
        ObjectMapper().writeValue(response.outputStream, responseParams)

        val jsonLog = JsonLog(loggerk)

        responseParams.forEach { (k, v) -> v?.let { jsonLog.add("$k", it) } }
        jsonLog.run(Level.WARN)
    }
}
