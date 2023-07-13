package com.softwareplace.springsecurity.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.jsonlogger.log.JsonLog
import com.softwareplace.jsonlogger.log.kLogger
import com.softwareplace.springsecurity.exception.ApiBaseException
import com.softwareplace.springsecurity.exception.IllegalConstraintsException
import com.softwareplace.springsecurity.extension.ThrowableExt.toResponse
import com.softwareplace.springsecurity.model.Response
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import org.slf4j.event.Level
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.core.NestedRuntimeException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.IOException
import java.time.LocalDateTime
import java.util.*


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ControllerAdvice(annotations = [RestController::class])
class ControllerExceptionAdvice(
    private val mapper: ObjectMapper,
    private val applicationInfo: ApplicationInfo
) : ResponseEntityExceptionHandler(), AccessDeniedHandler, AuthenticationEntryPoint {

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMediaTypeNotAcceptable(
        ex: HttpMediaTypeNotAcceptableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMissingPathVariable(
        ex: MissingPathVariableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleServletRequestBindingException(
        ex: ServletRequestBindingException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleConversionNotSupported(
        ex: ConversionNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleTypeMismatch(
        ex: TypeMismatchException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleHttpMessageNotWritable(
        ex: HttpMessageNotWritableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleMissingServletRequestPart(
        ex: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleAsyncRequestTimeoutException(
        ex: AsyncRequestTimeoutException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, status, ex, headers, request)
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return serverError(ex.message, statusCode, ex, headers, request)
    }

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, ex: AccessDeniedException) {
        accessDeniedRegister(request, response, ex)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authenticationException: AuthenticationException
    ) {
        accessDeniedRegister(request, response, authenticationException)
    }

    @ExceptionHandler(Exception::class)
    fun defaultErrorHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: Exception
    ): ResponseEntity<Response> {
        return serverError(request, response, ex)
    }

    @ExceptionHandler(ApiBaseException::class)
    fun baseExceptionErrorHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: ApiBaseException
    ): ResponseEntity<Response> {
        return serverError(request, response, ex)
    }


    @ExceptionHandler(ConstraintViolationException::class)
    fun exceptionHandler(request: HttpServletRequest, ex: ConstraintViolationException): ResponseEntity<Response> {
        val response = Response(
            errorInfo = ex.constraintViolations.associate {
                val propertyName = it.propertyPath.last().toString()
                Pair(propertyName, it.message)
            }
        )
        getLogger(ex, request)
            .error(ex)
            .message(ex.message ?: "")
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .run()
        return ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST)
    }

    fun serverError(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: Exception,
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        message: String = ex.message ?: "Failed to handle the request"
    ): ResponseEntity<Response> {

        val responseInfo = when (ex is ApiBaseException) {
            true -> ex.status to mapOf(
                "service" to request.requestURI,
            ).plus(ex.errorInfo)

            else -> status to mapOf(
                "service" to request.requestURI,
                "internalServerError" to true
            )
        }
        getLogger(ex, request)
            .add("status", responseInfo.first)
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .run()

        return ResponseEntity(
            Response(
                message = message,
                errorInfo = responseInfo.second
            ), responseInfo.first
        )
    }

    fun serverError(
        message: String? = null,
        status: HttpStatusCode,
        ex: Exception,
        headers: HttpHeaders,
        request: WebRequest
    ): ResponseEntity<Any> {
        val logMessage = message ?: "Failed to handle the request"
        getLogger(ex)
            .message(logMessage)
            .add("status", status.value())
            .add("date", LocalDateTime.now())
            .run()

        return ResponseEntity(
            Response(
                message = logMessage,
                errorInfo = mapOf(
                    "internalServerError" to true,
                )
            ), status
        )
    }


    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(response: HttpServletRequest, request: HttpServletRequest): ResponseEntity<*> {
        return unauthorizedAccess(request = request)
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException::class)
    fun handleAccessDeniedExceptionAuthentication(
        request: HttpServletRequest,
        ex: AuthenticationCredentialsNotFoundException
    ): ResponseEntity<*> {
        return unauthorizedAccess(ex, request)
    }


    @ExceptionHandler(NestedRuntimeException::class)
    fun dataIntegrityViolationExceptionHandler(
        request: HttpServletRequest,
        ex: NestedRuntimeException
    ): ResponseEntity<Response> {
        getLogger(ex, request)
            .error(ex)
            .message(ex.message ?: "")
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .run()
        return ResponseEntity<Response>(ex.toResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalConstraintsException::class)
    fun constraintViolationException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: IllegalConstraintsException
    ): ResponseEntity<*> {
        val infoMap = hashMapOf<String, Any>("badRequest" to true)
        infoMap.putAll(ex.errors)
        getLogger(ex, request)
            .message(ex.message ?: "Could not complete the request.")
            .add("date", LocalDateTime.now())
            .add("customProperties", infoMap)
            .run()

        return ResponseEntity(
            Response(
                errorInfo = infoMap,
                message = ex.message ?: "Could not complete the request."
            ), HttpStatus.BAD_REQUEST
        )
    }

    fun unauthorizedAccess(ex: Exception? = null, request: HttpServletRequest): ResponseEntity<Response> {
        getLogger(ex, request)
            .message("Unauthorized access")
            .add("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .run()

        return ResponseEntity(
            Response(
                message = "Unauthorized access",
                errorInfo = mapOf(
                    "service" to request.requestURI,
                    "unauthorizedAccess" to true
                )
            ), HttpStatus.UNAUTHORIZED
        )
    }

    fun accessDeniedRegister(request: HttpServletRequest, response: HttpServletResponse, ex: Throwable) {
        val mapBodyException = HashMap<String, Any>()
        mapBodyException["message"] = "Access denied"
        mapBodyException["timestamp"] = Date().time
        mapBodyException["success"] = false
        mapBodyException["service"] = request.requestURI
        mapBodyException["code"] = HttpServletResponse.SC_UNAUTHORIZED

        response.contentType = APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        getLogger(ex, request)
            .add("status", response.status)
            .error(ex)
            .message(ex.message ?: "")
            .add("service", request.requestURI)
            .add("date", LocalDateTime.now())
            .run()

        mapper.writeValue(response.outputStream, mapBodyException)
    }

    fun getLogger(
        ex: Throwable?,
        request: HttpServletRequest
    ) = baseJsonLogBuilder()
        .error(ex)
        .add("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
        .add("service", request.requestURI)

    fun getLogger(
        ex: Throwable?
    ): JsonLog {
        return baseJsonLogBuilder()
            .error(ex)
            .add("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
    }

    open fun baseJsonLogBuilder(): JsonLog {
        return if (applicationInfo.stackTraceLogEnable) {
            JsonLog(kLogger)
                .printStackTrackerEnable()
                .level(Level.ERROR)
        } else {
            JsonLog(kLogger)
                .level(Level.ERROR)
        }
    }
}


