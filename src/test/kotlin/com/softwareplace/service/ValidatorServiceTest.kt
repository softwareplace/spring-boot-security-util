package com.softwareplace.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.softwareplace.exception.IllegalConstraintsException;
import com.softwareplace.validator.ValidPassword;

@SpringBootTest
class ValidatorServiceTest {

	@Autowired
	private ValidatorService validatorService;

	@ParameterizedTest
	@ValueSource(strings = { "123", "sadfasf", "ADADSAFDF", "fdsafdsafdFIFDkldlfkjafdlkjafddD" })
	@DisplayName("must to throw IllegalConstraintsException when password is not valid")
	void validateTestCase01(String password) {
		assertThrows(IllegalConstraintsException.class, () -> validatorService.validate(new TestUser(password)));
	}

	@ParameterizedTest
	@ValueSource(strings = { "@uIddSbt4c", "*45Kdc@4lgb", "4#e23dcR", "@c1Fer$%3", "@Gvtd83*g" })
	@DisplayName("must no to throw IllegalConstraintsException when password is valid")
	void validateTestCase02(String password) {
		assertDoesNotThrow(() -> validatorService.validate(new TestUser(password)));
	}

	@ParameterizedTest
	@ValueSource(strings = { "123", "sadfasf", "ADADSAFDF", "fdsafdsafdFIFDkldlfkjafdlkjafddD" })
	@DisplayName("must to throw IllegalConstraintsException when password is not valid")
	void validateTestCase03(String password) {
		IllegalConstraintsException constraintsException = null;

		try {
			validatorService.validate(new TestUser(password));
		} catch (IllegalConstraintsException exception) {
			constraintsException = exception;
		}

		assertNotNull(constraintsException);
		assertFalse(constraintsException.getErrors().isEmpty());
		assertTrue(constraintsException.getErrors().containsKey("password"));
	}

	private static class TestUser {

		@ValidPassword
		private final String password;

		public TestUser(String password) {
			this.password = password;
		}
	}
}
