package com.softwareplace.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserData extends UserDetails {

	String authToken();

	String[] userRoles();

	default int role() {
		return Integer.MIN_VALUE;
	}
}
