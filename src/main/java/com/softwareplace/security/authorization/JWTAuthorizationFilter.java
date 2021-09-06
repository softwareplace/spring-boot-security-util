package com.softwareplace.security.authorization;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
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

import com.softwareplace.security.model.UserData;
import com.softwareplace.security.service.AuthorizationUserService;
import com.softwareplace.security.util.Constants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final AuthorizationUserService authorizationUserService;
	private final AuthorizationHandler authorizationHandler;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
			AuthorizationUserService authorizationUserService, AuthorizationHandler authorizationHandler) {
		super(authenticationManager);
		this.authorizationUserService = authorizationUserService;
		this.authorizationHandler = authorizationHandler;
	}

	@Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException {
		if (shouldBeAuthorized(request)) {
			try {
				SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthenticationToken(request));
				chain.doFilter(request, response);
			} catch (HttpClientErrorException.Unauthorized | MalformedJwtException | SignatureException exception) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				ResponseRegister.register(response);
			} catch (Exception exception) {
				ResponseRegister.register(response, "The request could not be completed.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new HashMap<>());
			}
		}
	}

	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(HttpServletRequest request) {
		String username = getUsername(request);
		if (username != null) {
			UserData userData = authorizationUserService.userData(username);
			if (userData != null && userData.userRoles() != null) {
				return getAuthenticationToken(request, userData);
			}
		}
		throw new AccessDeniedException("Access was not authorized on this request.");
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request, UserData userData) {
		List<SimpleGrantedAuthority> authorities = Arrays.stream(userData.userRoles())
				.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
				.collect(Collectors.toList());
		User principal = new User(userData.getUsername(), userData.getUsername(), authorities);
		authorizationHandler.authorizationSuccessfully(request, userData);
		return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
	}

	private String getUsername(HttpServletRequest request) throws UnsupportedOperationException {
		String authorization = request.getHeader(AUTHORIZATION)
				.replace("Bearer ", "");

		return Jwts.parser()
				.setSigningKey(authorizationUserService.secret())
				.parseClaimsJws(authorization)
				.getBody()
				.getSubject();
	}

	private boolean shouldBeAuthorized(HttpServletRequest request) {
		String requestHeader = request.getHeader(AUTHORIZATION);
		String requestURI = request.getRequestURI();
		return !(requestHeader == null ||
				requestURI.contains(Constants.OPEN_API_URL));
	}
}
