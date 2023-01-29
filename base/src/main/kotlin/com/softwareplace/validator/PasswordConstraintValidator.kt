package com.softwareplace.validator

import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.Rule
import java.lang.String.join
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.full.primaryConstructor

class PasswordConstraintValidator : ConstraintValidator<ValidPassword, String> {
    private var rules: List<Rule>? = null
    override fun initialize(constraintAnnotation: ValidPassword) {
        rules = try {
            constraintAnnotation.rulesBuilder.primaryConstructor!!.call().defaultRules()
        } catch (e: Exception) {
            e.printStackTrace()
            RuleBuilderImpl().defaultRules()
        }
    }

    override fun isValid(password: String?, context: ConstraintValidatorContext): Boolean {
        val validator = PasswordValidator(rules)
        val result = validator.validate(PasswordData(password))

        if (result.isValid) {
            return true
        }

        val messages = validator.getMessages(result)
        val messageTemplate = join(",", messages)
        context.buildConstraintViolationWithTemplate(messageTemplate)
            .addConstraintViolation()
            .disableDefaultConstraintViolation()
        return false
    }
}