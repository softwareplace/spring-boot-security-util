package com.softwareplace.validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.passay.LengthRule;
import org.passay.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PasswordConstraintValidatorTest {

	public static final int MIN_LENGTH = 8;
	public static final int MAX_LENGTH = 30;

	@Autowired
	private Validator validator;

	@ParameterizedTest
	@ValueSource(strings = { "1", "12", "123", "1234", "12345", "123456", "1234567" })
	@DisplayName("must to return password length constraint violation message with the minimum password requirements")
	void passwordValidationTestCase01(String password) {
		final TestUser testUser = new TestUser(password);
		final Set<ConstraintViolation<TestUser>> constraintViolations = validator.validate(testUser);

		assertNotNull(constraintViolations);
		final List<String> constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations);

		assertTrue(constraintViolationMessages.contains("Password must be 8 or more characters in length."));
	}

	@ParameterizedTest
	@ValueSource(strings = { "122165464654897864654654697987967498798" })
	@DisplayName("must to return password length constraint violation message with the maximum password requirements")
	void passwordValidationTestCase02(String password) {
		final TestUser testUser = new TestUser(password);
		final Set<ConstraintViolation<TestUser>> constraintViolations = validator.validate(testUser);

		assertNotNull(constraintViolations);
		final List<String> constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations);
		assertTrue(constraintViolationMessages.contains("Password must be no more than 30 characters in length."));
	}

	@ParameterizedTest
	@ValueSource(strings = { "12216abcdefgh", "abcdefgh_12216" })
	@DisplayName("must to contains message with uppercase characters missing")
	void passwordValidationTestCase03(String password) {
		final TestUser testUser = new TestUser(password);
		final Set<ConstraintViolation<TestUser>> constraintViolations = validator.validate(testUser);

		assertNotNull(constraintViolations);
		final List<String> constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations);
		assertTrue(constraintViolationMessages.contains("Password must contain 1 or more uppercase characters."));
	}

	@ParameterizedTest
	@ValueSource(strings = { "DALKJFDJFDKJD", "DALKJFDJFDKJD12216@" })
	@DisplayName("must to contains message with lowercase characters missing")
	void passwordValidationTestCase04(String password) {
		final TestUser testUser = new TestUser(password);
		final Set<ConstraintViolation<TestUser>> constraintViolations = validator.validate(testUser);

		assertNotNull(constraintViolations);
		final List<String> constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations);
		assertTrue(constraintViolationMessages.contains("Password must contain 1 or more lowercase characters."));
	}

	@ParameterizedTest
	@ValueSource(strings = { "123456DKtsatsa", "123456" })
	@DisplayName("must to contains message with special characters missing")
	void passwordValidationTestCase05(String password) {
		final TestUser testUser = new TestUser(password);
		final Set<ConstraintViolation<TestUser>> constraintViolations = validator.validate(testUser);

		assertNotNull(constraintViolations);
		final List<String> constraintViolationMessages = getMessagesFromConstraintViolation(constraintViolations);
		constraintViolationMessages.forEach(System.out::println);
		assertTrue(constraintViolationMessages.contains("Password must contain 1 or more special characters."));
	}

	private List<String> getMessagesFromConstraintViolation(Set<ConstraintViolation<TestUser>> constraintViolations) {
		final List<String> constraintViolationMessages = new ArrayList<>();
		constraintViolations.forEach(constraintViolation -> {
			final List<String> messages = Arrays.stream(constraintViolation.getMessageTemplate().split(","))
					.collect(Collectors.toList());
			constraintViolationMessages.addAll(messages);
		});
		return constraintViolationMessages;
	}

	private static class TestUser {

		@ValidPassword(rulesBuilder = TestRuleBuilderImplTest.class)
		private String password;

		public TestUser(String password) {
			this.password = password;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public static class TestRuleBuilderImplTest extends RuleBuilderImpl {

		@Override public Rule lengthRule() {
			return new LengthRule(MIN_LENGTH, MAX_LENGTH);
		}
	}
}
