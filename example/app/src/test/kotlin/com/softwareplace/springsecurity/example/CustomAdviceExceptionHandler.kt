package com.softwareplace.springsecurity.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.springsecurity.config.AdviceExceptionHandler
import com.softwareplace.springsecurity.config.ApplicationInfo
import com.softwareplace.springsecurity.model.Response
import org.springframework.core.NestedRuntimeException
import org.springframework.stereotype.Component


@Component
class CustomAdviceExceptionHandler(
    mapper: ObjectMapper,
    applicationInfo: ApplicationInfo,
) : AdviceExceptionHandler(mapper, applicationInfo) {

    override fun toResponse(ex: NestedRuntimeException): Response {
        val response = super.toResponse(ex)
        if (response.errorInfo.containsKey("duplicated_Cnpj")) {
            val errorInfo = response.errorInfo.toMutableMap()
            errorInfo.remove("duplicated_Cnpj")
            errorInfo["unavailableCnpj"] = "This Cnpj is not available anymore, please try another one"
            return response.copy(errorInfo = errorInfo)
        }
        return response
    }


}
