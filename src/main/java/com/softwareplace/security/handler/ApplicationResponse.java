package com.softwareplace.security.handler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ApplicationResponse {
	private final int code;
	private final boolean success;
	private final String message;
	private final long timestamp;

	public ApplicationResponse(int code, boolean success, String message) {
		this.code = code;
		this.success = success;
		this.message = message;
		this.timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}

	public ApplicationResponse(int code, String message) {
		this.code = code;
		this.success = false;
		this.message = message;
		this.timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}
}
