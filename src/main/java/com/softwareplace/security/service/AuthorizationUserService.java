package com.softwareplace.security.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.softwareplace.security.model.RequestUser;
import com.softwareplace.security.model.UserData;

public interface AuthorizationUserService {

	UserData userData(RequestUser user);

	UserData userData(String username);

	long expirationTime();

	String secret();

	default void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

	}

	default Map<String, Object> claims(HttpServletRequest httpServletRequest, UserData userData) {
		return new HashMap<>();
	}
}
