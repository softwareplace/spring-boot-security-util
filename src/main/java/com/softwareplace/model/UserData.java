package com.softwareplace.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserData extends UserDetails {

	String authToken();

	String[] userRoles();

	default int role() {
		return Integer.MIN_VALUE;
	}

	@Override default boolean isAccountNonExpired() {
		return true;
	}

	@Override default boolean isAccountNonLocked() {
		return true;
	}

	@Override default boolean isCredentialsNonExpired() {
		return true;
	}
}
