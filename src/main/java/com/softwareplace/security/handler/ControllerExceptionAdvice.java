package com.softwareplace.security.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.softwareplace.security.authorization.ResponseRegister;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

	public static final String COULD_NOT_COMPLETE_THE_REQUEST = "Could not complete the request.";
	public static final String UNAUTHORIZED_ACCESS = "Access denied.";

	@Override protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return errorResponse(request, ex, HttpStatus.NOT_FOUND, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@Override protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse(request, ex, HttpStatus.BAD_REQUEST, COULD_NOT_COMPLETE_THE_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(WebRequest request, Exception exception) {
		return errorResponse(request, exception, HttpStatus.UNAUTHORIZED, UNAUTHORIZED_ACCESS);
	}

	@Override public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception)
			throws IOException {
		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		ResponseRegister.register(httpServletResponse, COULD_NOT_COMPLETE_THE_REQUEST, HttpServletResponse.SC_BAD_REQUEST, Map.of());
	}

	@Override public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException exception)
			throws IOException {
		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		ResponseRegister.register(httpServletResponse);
	}

	private ResponseEntity<Object> errorResponse(WebRequest request, Exception exception, HttpStatus statusCode, String message) {
		return new ResponseEntity<>(new ApplicationResponse(statusCode.value(), message), statusCode);
	}

}
