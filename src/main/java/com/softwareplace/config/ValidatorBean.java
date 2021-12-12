package com.softwareplace.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ValidatorBean {

	@Bean
	public Validator validator() {
		return Validation.buildDefaultValidatorFactory()
				.getValidator();
	}
}
