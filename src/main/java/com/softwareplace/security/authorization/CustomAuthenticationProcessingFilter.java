package com.softwareplace.security.authorization;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

abstract class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
	protected CustomAuthenticationProcessingFilter() {
		super(new AntPathRequestMatcher("/authorization", "POST"));
	}
}
