package com.softwareplace.springsecurity.validator

import com.softwareplace.springsecurity.validator.annotation.ValidEmail
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailConstraintValidator : ConstraintValidator<ValidEmail, String?> {

    private lateinit var errorMessage: String
    private lateinit var regex: Regex

    override fun initialize(constraintAnnotation: ValidEmail) {
        errorMessage = constraintAnnotation.message
        regex = Regex(constraintAnnotation.regex)
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value != null && value.matches(regex)) {
            return true
        }

        context.buildConstraintViolationWithTemplate(errorMessage)
            .addConstraintViolation()
            .disableDefaultConstraintViolation()
        return false
    }
}
