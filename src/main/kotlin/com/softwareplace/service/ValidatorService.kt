package com.softwareplace.service

import com.softwareplace.exception.IllegalConstraintsException
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.ConstraintViolation
import javax.validation.Validator

open class ValidatorService constructor(
    private val validator: Validator
) {

    @Throws(IllegalConstraintsException::class)
    open fun <T> validate(data: T) {
        val constraintViolations = validator.validate(data)
        if (constraintViolations.isNotEmpty()) {
            throw IllegalConstraintsException("Invalid data input", getMessagesFromConstraintViolation(constraintViolations))
        }
    }

    open fun <T> getMessagesFromConstraintViolation(constraintViolations: Set<ConstraintViolation<T>>): Map<String, MutableList<String>> {
        val errors = HashMap<String, MutableList<String>>()
        constraintViolations.forEach(Consumer { constraintViolation: ConstraintViolation<T> ->
            val messages = Arrays.stream(constraintViolation.messageTemplate.split(",".toRegex()).toTypedArray())
                .collect(Collectors.toList())
            messages.forEach(Consumer { message: String ->
                val fieldName = Arrays.stream(message.split("\\s".toRegex()).toTypedArray()).findFirst()
                fieldName.ifPresent { value: String ->
                    errors.computeIfAbsent(value.lowercase()) { ArrayList() }
                        .add(message)
                }
            })
        })
        return errors
    }
}
