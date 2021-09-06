package com.softwareplace.security.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserData extends UserDetails {

	String authToken();

	String[] userRoles();
}
