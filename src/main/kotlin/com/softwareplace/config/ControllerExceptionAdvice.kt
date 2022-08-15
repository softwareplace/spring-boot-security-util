package com.softwareplace.config

import br.com.softwareplace.json.logger.log.JsonLog
import br.com.softwareplace.json.logger.log.loggerk
import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.exception.IllegalConstraintsException
import com.softwareplace.model.Response
import org.apache.logging.log4j.Level
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.IOException
import java.time.LocalDateTime
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class ControllerExceptionAdvice : ResponseEntityExceptionHandler(), AccessDeniedHandler, AuthenticationEntryPoint {

    override fun handleHttpRequestMethodNotSupported(
            ex: HttpRequestMethodNotSupportedException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMediaTypeNotSupported(
            ex: HttpMediaTypeNotSupportedException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMediaTypeNotAcceptable(
            ex: HttpMediaTypeNotAcceptableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMissingPathVariable(
            ex: MissingPathVariableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMissingServletRequestParameter(
            ex: MissingServletRequestParameterException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleServletRequestBindingException(
            ex: ServletRequestBindingException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleConversionNotSupported(
            ex: ConversionNotSupportedException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleTypeMismatch(ex: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMessageNotReadable(
            ex: HttpMessageNotReadableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMessageNotWritable(
            ex: HttpMessageNotWritableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMissingServletRequestPart(
            ex: MissingServletRequestPartException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleNoHandlerFoundException(
            ex: NoHandlerFoundException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleAsyncRequestTimeoutException(
            ex: AsyncRequestTimeoutException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleExceptionInternal(ex: Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, ex: AccessDeniedException) {
        accessDeniedRegister(request, response)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authenticationException: AuthenticationException) {
        accessDeniedRegister(request, response)
    }

    @ExceptionHandler(Exception::class)
    fun defaultErrorHandler(request: HttpServletRequest, response: HttpServletResponse, ex: Exception): ResponseEntity<Response> {
        return serverError(request, response, ex)
    }

    open fun serverError(request: HttpServletRequest, response: HttpServletResponse, ex: Exception): ResponseEntity<Response> {
        val logMessage = ex.message ?: "Failed to handle the request"

        JsonLog(loggerk)
                .message(logMessage)
                .add("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .add("service", request.requestURI)
                .add("date", LocalDateTime.now())
                .error(ex)
                .run(Level.ERROR)

        return ResponseEntity(
                Response(
                        message = logMessage,
                        info = mapOf(
                                "service" to request.requestURI,
                                "internalServerError" to true
                        )
                ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    open fun serverError(message: String? = null, status: HttpStatus, ex: Exception, headers: HttpHeaders, request: WebRequest): ResponseEntity<Any> {
        val logMessage = message ?: "Failed to handle the request"

        JsonLog(loggerk)
                .message(logMessage)
                .add("status", status.value())
                .add("date", LocalDateTime.now())
                .error(ex)
                .run(Level.ERROR)

        return ResponseEntity(
                Response(
                        message = logMessage,
                        info = mapOf(
                                "internalServerError" to true,
                        )
                ), status
        )
    }

    @ExceptionHandler(AccessDeniedException::class)
    open fun handleAccessDeniedException(response: HttpServletRequest, request: HttpServletRequest): ResponseEntity<*> {
        return unauthorizedAccess(request = request)
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException::class)
    open fun handleAccessDeniedExceptionAuthentication(request: HttpServletRequest, ex: AuthenticationCredentialsNotFoundException): ResponseEntity<*> {
        return unauthorizedAccess(ex, request)
    }

    @ExceptionHandler(IllegalConstraintsException::class)
    fun constraintViolationException(request: HttpServletRequest, ex: IllegalConstraintsException): ResponseEntity<*> {
        val infoMap = hashMapOf<String, Any>("badRequest" to true)
        infoMap.putAll(ex.errors)

        JsonLog(loggerk)
                .message(ex.message ?: "Could not complete the request.")
                .add("status", HttpStatus.BAD_REQUEST.value())
                .add("service", request.requestURI)
                .add("date", LocalDateTime.now())
                .add("customProperties", infoMap)
                .error(ex)
                .run(Level.ERROR)

        return ResponseEntity(
                Response(
                        info = infoMap,
                        message = ex.message ?: "Could not complete the request."
                ), HttpStatus.BAD_REQUEST
        )
    }

    open fun unauthorizedAccess(ex: Exception? = null, request: HttpServletRequest): ResponseEntity<Response> {

        JsonLog(loggerk)
                .message("Unauthorized access")
                .add("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .add("service", request.requestURI)
                .add("date", LocalDateTime.now())
                .error(ex)
                .run(Level.ERROR)


        return ResponseEntity(
                Response(
                        message = "Unauthorized access",
                        info = mapOf(
                                "service" to request.requestURI,
                                "unauthorizedAccess" to true
                        )
                ), HttpStatus.UNAUTHORIZED
        )
    }

    open fun accessDeniedRegister(request: HttpServletRequest, response: HttpServletResponse) {
        val mapBodyException = HashMap<String, Any>()
        mapBodyException["message"] = "Access denied"
        mapBodyException["timestamp"] = Date().time
        mapBodyException["success"] = false
        mapBodyException["service"] = request.requestURI
        mapBodyException["code"] = HttpServletResponse.SC_UNAUTHORIZED

        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val mapper = ObjectMapper()
        mapper.writeValue(response.outputStream, mapBodyException)
    }
}
