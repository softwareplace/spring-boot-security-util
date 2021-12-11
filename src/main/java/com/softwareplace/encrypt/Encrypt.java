package com.softwareplace.encrypt;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrypt {
	public static final int ENCODER_STRENGTH = 5;
	public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(ENCODER_STRENGTH);

	private final String password;
	private final String encodedPassword;
	private final String token;
	private final String authToken;
	private final String salt;

	public Encrypt(String password) {
		this.password = password;
		this.encodedPassword = PASSWORD_ENCODER.encode(password);
		this.token = PASSWORD_ENCODER.encode(encodedPassword + mixedString());
		this.authToken = PASSWORD_ENCODER.encode(token);
		this.salt = PASSWORD_ENCODER.encode(this.authToken + this.token);
	}

	private String mixedString() {
		int randomResult = new Random().nextInt(100_000);
		return String.valueOf(System.currentTimeMillis() + randomResult);
	}

	public boolean isValidPassword(String encodedPassword) {
		return PASSWORD_ENCODER.matches(this.password, encodedPassword);
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public String getToken() {
		return token;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getSalt() {
		return salt;
	}
}
