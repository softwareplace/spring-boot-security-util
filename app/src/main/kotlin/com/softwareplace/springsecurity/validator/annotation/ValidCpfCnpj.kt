package com.softwareplace.springsecurity.validator.annotation

import com.softwareplace.springsecurity.validator.CpfCnpjConstraintValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@MustBeDocumented
@ReportAsSingleViolation
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CpfCnpjConstraintValidator::class])
annotation class ValidCpfCnpj(
    val message: String = "CPF or CNPJ is not valid",
    val onErrorUseName: String = "",
    val acceptOnly: Type = Type.ALL,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    enum class Type {
        ALL,
        CPF,
        CNPJ
    }
}
