package com.softwareplace.security.authorization;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwareplace.security.encrypt.Encrypt;
import com.softwareplace.security.model.RequestUser;
import com.softwareplace.security.model.UserData;
import com.softwareplace.security.service.AuthorizationUserService;

public class JWTAuthenticationFilter extends CustomAuthenticationProcessingFilter {
	private static final String ACCESS_TOKEN = "accessToken";
	private static final String SUB = "sub";

	private final AuthorizationUserService authorizationUserService;
	private final AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthorizationUserService authorizationUserService, AuthenticationManager authenticationManager) {
		this.authorizationUserService = authorizationUserService;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws AuthenticationException, IOException {
		RequestUser requestUser = new ObjectMapper().readValue(httpServletRequest.getInputStream(), RequestUser.class);
		UserData userData = this.authorizationUserService.userData(requestUser);
		if (userData != null) {
			Encrypt encrypt = new Encrypt(requestUser.getPassword());
			if (encrypt.isValidPassword(userData.getPassword())) {
				Map<String, Object> claims = authorizationUserService.claims(httpServletRequest);

				if (userData.role() != Integer.MIN_VALUE) {
					claims.put("role", userData.role());
				}
				JWTGenerate jwtGenerate = new JWTGenerate(authorizationUserService);
				httpServletRequest.setAttribute(ACCESS_TOKEN, jwtGenerate.tokenGenerate(claims, userData.authToken()));
				return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userData.authToken(), requestUser.getPassword()));
			}
		}

		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ResponseRegister.register(httpServletResponse);

		return null;
	}

	@Override protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException {
		authorizationUserService.successfulAuthentication(request, response, chain, authResult);
	}
}
