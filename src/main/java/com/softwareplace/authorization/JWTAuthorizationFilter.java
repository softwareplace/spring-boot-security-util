package com.softwareplace.authorization;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import com.softwareplace.model.UserData;
import com.softwareplace.service.AuthorizationUserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public static final String BEARER = "Bearer ";
	public static final String UNAUTHORIZED_ERROR_MESSAGE = "Access was not authorized on this request.";
	public static final String ROLE = "ROLE_";
	public static final String ERROR_RESPONSE_MESSAGE = "The request could not be completed.";

	private final AuthorizationUserService authorizationUserService;
	private final AuthorizationHandler authorizationHandler;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
			AuthorizationUserService authorizationUserService, AuthorizationHandler authorizationHandler) {
		super(authenticationManager);
		this.authorizationUserService = authorizationUserService;
		this.authorizationHandler = authorizationHandler;
	}

	@Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthenticationToken(request));
			chain.doFilter(request, response);
		} catch (HttpClientErrorException.Unauthorized | MalformedJwtException | AccessDeniedException | SignatureException exception) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ResponseRegister.register(response);
			authorizationHandler.onAuthorizationFailed(request, response, chain, exception);
		} catch (Exception exception) {
			ResponseRegister.register(response, ERROR_RESPONSE_MESSAGE, HttpServletResponse.SC_BAD_REQUEST, new HashMap<>());
			authorizationHandler.onAuthorizationFailed(request, response, chain, exception);
		}
	}

	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(HttpServletRequest request) {
		String authToken = getAuthToken(request);
		if (authToken != null) {
			UserData userData = authorizationUserService.userData(authToken);
			if (userData != null && userData.userRoles() != null) {
				return getAuthenticationToken(request, userData);
			}
		}
		throw new AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE);
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request, UserData userData) {
		List<SimpleGrantedAuthority> authorities = Arrays.stream(userData.userRoles())
				.map(role -> new SimpleGrantedAuthority(ROLE.concat(role)))
				.collect(Collectors.toList());
		User principal = new User(userData.getUsername(), userData.getUsername(), authorities);
		authorizationHandler.authorizationSuccessfully(request, userData);
		return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
	}

	private String getAuthToken(HttpServletRequest request) throws UnsupportedOperationException {
		String requestHeader = request.getHeader(AUTHORIZATION);
		if (requestHeader != null) {
			String authorization = requestHeader
					.replace(BEARER, "");
			return Jwts.parser()
					.setSigningKey(authorizationUserService.secret())
					.parseClaimsJws(authorization)
					.getBody()
					.getSubject();
		}
		throw new AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE);
	}
}
