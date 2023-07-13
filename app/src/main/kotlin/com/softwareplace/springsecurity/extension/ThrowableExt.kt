package com.softwareplace.springsecurity.extension

import com.softwareplace.springsecurity.model.Response
import jakarta.validation.ValidationException
import org.springframework.core.NestedRuntimeException
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

object ThrowableExt {

    val NestedRuntimeException.toResponse
        get() = run {
            val errorsMessage = HashMap<String, Any>()
            val response = Response(errorInfo = errorsMessage)
            getErrors(errorsMessage)
            response
        }

    fun Throwable.getErrors(messageMap: HashMap<String, Any>) {
        if (this is ValidationException) {
            validationErrors(messageMap)
        }

        message?.run {
            if (contains("Detail: Key (") && contains(") already exists.")) {
                messageMap["badRequest"] = true
                val patternKey = Pattern.compile("\\((.*?)\\)")
                val patternValue = Pattern.compile("=\\((.*?)\\)")
                val matcherKey = patternKey.matcher(this)
                val matcherValue = patternValue.matcher(this)
                if (matcherKey.find() && matcherValue.find()) {
                    val key = matcherKey.group(1)
                    val value = matcherValue.group(1)
                    messageMap[key.addAtStartAsCamelCase("duplicated_")] = value
                }
            }
        }

        cause?.getErrors(messageMap)
    }

    fun ValidationException.validationErrors(messageMap: HashMap<String, Any>) {
        message?.let {
            val messages = Arrays.stream(it.split("ConstraintViolationImpl\\{".toRegex()).toTypedArray())
                .filter { message -> message.contains("propertyPath=") }
                .map { message -> message.replace("interpolatedMessage='", "") }
                .map { message -> message.split("propertyPath=").first() }
                .map { message -> message.replace("',", "") }
                .collect(Collectors.toList())

            messages.forEach { stringMessages ->
                val values = stringMessages.split(",")

                if (values.isNotEmpty()) {
                    val fieldName = Arrays.stream(values[0].split("\\s".toRegex()).toTypedArray()).findFirst()
                    fieldName.ifPresent { value -> messageMap[value] = values }
                }
            }
        }
    }
}
