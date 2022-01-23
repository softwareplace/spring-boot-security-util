package com.softwareplace.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.exception.IllegalConstraintsException
import com.softwareplace.model.Response
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
        return serverError(ex.message, status)
    }

    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleHttpMediaTypeNotAcceptable(
        ex: HttpMediaTypeNotAcceptableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleMissingPathVariable(
        ex: MissingPathVariableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleServletRequestBindingException(
        ex: ServletRequestBindingException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleConversionNotSupported(
        ex: ConversionNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleTypeMismatch(ex: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleHttpMessageNotWritable(
        ex: HttpMessageNotWritableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleMissingServletRequestPart(
        ex: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleAsyncRequestTimeoutException(
        ex: AsyncRequestTimeoutException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    override fun handleExceptionInternal(ex: Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return serverError(ex.message, status)
    }

    open fun serverError(message: String? = null, status: HttpStatus): ResponseEntity<Any> =
        ResponseEntity(
            Response(
                message = message ?: "Could not complete your request request",
                info = mapOf("internalServerError" to true)
            ), status
        )


    @ExceptionHandler(AccessDeniedException::class)
    open fun handleAccessDeniedException(response: HttpServletRequest): ResponseEntity<*> {
        return unauthorizedAccess()
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException::class)
    open fun handleAccessDeniedExceptionAuthentication(request: WebRequest?, ex: AuthenticationCredentialsNotFoundException): ResponseEntity<*> {
        return unauthorizedAccess(ex)
    }

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, ex: AccessDeniedException) {
        accessDeniedRegister(response)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authenticationException: AuthenticationException) {
        accessDeniedRegister(response)
    }

    @ExceptionHandler(IllegalConstraintsException::class)
    fun constraintViolationException(request: HttpServletRequest, ex: IllegalConstraintsException): ResponseEntity<*> {
        val infoMap = hashMapOf<String, Any>("badRequest" to true)
        infoMap.putAll(ex.errors)
        return ResponseEntity(
            Response(
                info = infoMap,
                message = ex.message ?: "Could not complete the request."
            ), HttpStatus.BAD_REQUEST
        )
    }

    open fun unauthorizedAccess(ex: Exception? = null) = ResponseEntity(
        Response(
            message = "Unauthorized access",
            info = mapOf("unauthorizedAccess" to true)
        ), HttpStatus.UNAUTHORIZED
    )

    open fun accessDeniedRegister(response: HttpServletResponse) {
        val mapBodyException = HashMap<String, Any>()
        mapBodyException["message"] = "Access denied"
        mapBodyException["timestamp"] = Date().time
        mapBodyException["success"] = false
        mapBodyException["code"] = HttpServletResponse.SC_UNAUTHORIZED

        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val mapper = ObjectMapper()
        mapper.writeValue(response.outputStream, mapBodyException)
    }
}
