package com.softwareplace.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softwareplace.exception.IllegalConstraintsException;

@Component
public class ValidatorService {

	private final Validator validator;

	@Autowired
	public ValidatorService(Validator validator) {
		this.validator = validator;
	}

	public <T> void validate(T data) throws IllegalConstraintsException {
		final Set<ConstraintViolation<T>> constraintViolations = validator.validate(data);
		if (!constraintViolations.isEmpty()) {
			throw new IllegalConstraintsException("", getMessagesFromConstraintViolation(constraintViolations));
		}
	}

	private <T> List<String> getMessagesFromConstraintViolation(Set<ConstraintViolation<T>> constraintViolations) {
		final List<String> constraintViolationMessages = new ArrayList<>();
		constraintViolations.forEach(constraintViolation -> {
			final List<String> messages = Arrays.stream(constraintViolation.getMessageTemplate().split(","))
					.collect(Collectors.toList());
			constraintViolationMessages.addAll(messages);
		});
		return constraintViolationMessages;
	}
}
