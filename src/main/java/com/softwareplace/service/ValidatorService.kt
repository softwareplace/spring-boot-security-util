package com.softwareplace.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softwareplace.exception.IllegalConstraintsException;

@Service
public class ValidatorService {

	private final Validator validator;

	@Autowired
	public ValidatorService(Validator validator) {
		this.validator = validator;
	}

	public <T> void validate(T data) throws IllegalConstraintsException {
		final Set<ConstraintViolation<T>> constraintViolations = validator.validate(data);
		if (!constraintViolations.isEmpty()) {
			throw new IllegalConstraintsException("Invalid data input", getMessagesFromConstraintViolation(constraintViolations));
		}
	}

	private <T> Map<String, List<String>> getMessagesFromConstraintViolation(Set<ConstraintViolation<T>> constraintViolations) {
		final HashMap<String, List<String>> errors = new HashMap<>();

		constraintViolations.forEach(constraintViolation -> {
			final List<String> messages = Arrays.stream(constraintViolation.getMessageTemplate().split(","))
					.collect(Collectors.toList());

			messages.forEach(message -> {
				final Optional<String> fieldName = Arrays.stream(message.split("\\s")).findFirst();
				fieldName.ifPresent(value -> errors.computeIfAbsent(value.toLowerCase(Locale.ROOT), key -> new ArrayList<>())
						.add(message));
			});
		});
		return errors;
	}
}
