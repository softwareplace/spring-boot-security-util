package com.softwareplace.exception;

import java.util.List;

public class IllegalConstraintsException extends Exception {

	private final List<String> errors;

	public IllegalConstraintsException(String message, List<String> errors) {
		super(message);
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}
