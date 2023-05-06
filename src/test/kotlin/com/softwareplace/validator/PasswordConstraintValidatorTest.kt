package com.softwareplace.validator

import com.softwareplace.App
import com.softwareplace.springsecurity.validator.RuleBuilderImpl
import com.softwareplace.springsecurity.validator.ValidPassword
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.passay.LengthRule
import org.passay.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

@SpringBootTest(classes = [App::class])
internal class PasswordConstraintValidatorTest {

    @Autowired
    private lateinit var validator: Validator

    @ParameterizedTest
    @ValueSource(strings = ["1", "12", "123", "1234", "12345", "123456", "1234567"])
    fun `must to return password length constraint violation message with the minimum password requirements`(password: String) {
        val testUser = TestUser(password)
        val constraintViolations: MutableSet<ConstraintViolation<TestUser>> = validator.validate(testUser)
        assertNotNull(constraintViolations)
        val constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations)
        assertTrue(constraintViolationMessages.contains("Password must be 8 or more characters in length."))
    }

    @ParameterizedTest
    @ValueSource(strings = ["122165464654897864654654697987967498798"])
    fun `must to return password length constraint violation message with the maximum password requirements`(password: String) {
        val testUser = TestUser(password)
        val constraintViolations = validator.validate(testUser)
        assertNotNull(constraintViolations)
        val constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations)
        assertTrue(constraintViolationMessages.contains("Password must be no more than 30 characters in length."))
    }

    @ParameterizedTest
    @ValueSource(strings = ["12216abcdefgh", "abcdefgh_12216"])
    fun `must to contains message with uppercase characters missing`(password: String?) {
        val testUser = TestUser(password)
        val constraintViolations = validator.validate(testUser)
        assertNotNull(constraintViolations)
        val constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations)
        assertTrue(constraintViolationMessages.contains("Password must contain 1 or more uppercase characters."))
    }

    @ParameterizedTest
    @ValueSource(strings = ["DALKJFDJFDKJD", "DALKJFDJFDKJD12216@"])
    fun `must to contains message with lowercase characters missing`(password: String) {
        val testUser = TestUser(password)
        val constraintViolations = validator.validate(testUser)
        assertNotNull(constraintViolations)
        val constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations)
        assertTrue(constraintViolationMessages.contains("Password must contain 1 or more lowercase characters."))
    }

    @ParameterizedTest
    @ValueSource(strings = ["123456DKtsatsa", "123456"])
    fun `must to contains message with special characters missing`(password: String) {
        val testUser = TestUser(password)
        val constraintViolations = validator.validate(testUser)
        assertNotNull(constraintViolations)
        val constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations)
        assertTrue(constraintViolationMessages.contains("Password must contain 1 or more special characters."))
    }

    private fun getMessagesFromConstraintViolation(constraintViolations: Set<ConstraintViolation<TestUser>>): List<String> {
        val constraintViolationMessages: MutableList<String> = ArrayList()
        constraintViolations.forEach(Consumer { constraintViolation: ConstraintViolation<TestUser> ->
            val messages = Arrays.stream(constraintViolation.messageTemplate.split(",".toRegex()).toTypedArray())
                .collect(Collectors.toList())
            constraintViolationMessages.addAll(messages)
        })
        return constraintViolationMessages
    }

    private class TestUser(@field:ValidPassword(rulesBuilder = TestRuleBuilderImplTest::class) var password: String?)
    class TestRuleBuilderImplTest : RuleBuilderImpl() {
        override fun lengthRule(): Rule {
            return LengthRule(MIN_LENGTH, MAX_LENGTH)
        }
    }

    companion object {
        const val MIN_LENGTH = 8
        const val MAX_LENGTH = 30
    }
}
