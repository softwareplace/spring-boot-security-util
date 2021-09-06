package com.softwareplace.security.authorization;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softwareplace.security.model.UserData;

public interface AuthorizationHandler {

	void authorizationSuccessfully(HttpServletRequest request, UserData userData);

	void onAuthorizationFailed(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Exception exception);
}
