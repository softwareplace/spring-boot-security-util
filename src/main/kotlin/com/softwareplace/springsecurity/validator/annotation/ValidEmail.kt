package com.softwareplace.springsecurity.validator.annotation

import com.softwareplace.springsecurity.validator.EmailConstraintValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@MustBeDocumented
@ReportAsSingleViolation
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailConstraintValidator::class])
annotation class ValidEmail(
    val message: String = "Email address is not valid",
    val regex: String = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
