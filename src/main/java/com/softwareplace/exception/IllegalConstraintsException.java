package com.softwareplace.exception;

import java.util.List;
import java.util.Map;

public class IllegalConstraintsException extends Exception {

	private final Map<String, List<String>> errors;

	public IllegalConstraintsException(String message, Map<String, List<String>> errors) {
		super(message);
		this.errors = errors;
	}

	public Map<String, List<String>> getErrors() {
		return errors;
	}
}
