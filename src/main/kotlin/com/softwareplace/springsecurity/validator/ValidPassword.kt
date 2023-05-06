package com.softwareplace.springsecurity.validator


import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [PasswordConstraintValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidPassword(

    /**
     * The class that implements on [RuleBuilder] must to
     * contains a public constructor with no arguments
     */
    val rulesBuilder: KClass<out RuleBuilder> = RuleBuilderImpl::class,
    val message: String = "Invalid Password",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
