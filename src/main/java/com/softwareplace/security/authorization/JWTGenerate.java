package com.softwareplace.security.authorization;

import java.util.Date;
import java.util.Map;

import com.softwareplace.security.service.AuthorizationUserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTGenerate {
	private final AuthorizationUserService authorizationUserService;

	public JWTGenerate(AuthorizationUserService authorizationUserService) {
		this.authorizationUserService = authorizationUserService;
	}

	public String tokenGenerate(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + authorizationUserService.expirationTime()))
				.signWith(SignatureAlgorithm.HS512, authorizationUserService.secret())
				.setHeaderParams(Map.ofEntries(Map.entry("typ", "JWT")))
				.compact();
	}
}
