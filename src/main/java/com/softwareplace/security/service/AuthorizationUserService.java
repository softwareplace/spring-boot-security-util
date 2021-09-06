package com.softwareplace.security.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.softwareplace.security.authorization.ResponseRegister;
import com.softwareplace.security.model.RequestUser;
import com.softwareplace.security.model.UserData;

public interface AuthorizationUserService {

	UserData userData(RequestUser user);

	UserData userData(String username);

	long expirationTime();

	String secret();

	default void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("jwt", request.getAttribute("accessToken"));
		params.put("success", true);
		ResponseRegister.register(response, "Authorization successful.", 200, params);
	}

	default Map<String, Object> claims(HttpServletRequest httpServletRequest) {
		return new HashMap<>();
	}
}
