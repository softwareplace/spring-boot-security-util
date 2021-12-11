package com.softwareplace.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.softwareplace.model.RequestUser;
import com.softwareplace.model.UserData;

public interface AuthorizationUserService {

	UserData userData(RequestUser user);

	UserData userData(String authToken);

	long expirationTime();

	String secret();

	default void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

	}

	default Map<String, Object> claims(HttpServletRequest httpServletRequest, UserData userData) {
		return new HashMap<>();
	}
}
