package com.softwareplace.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

	private static class TestUser {

		@ValidPassword
		private final String password;

		public TestUser(String password) {
			this.password = password;
		}
	}
}
