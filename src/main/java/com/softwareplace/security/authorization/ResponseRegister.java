package com.softwareplace.security.authorization;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseRegister {

	private ResponseRegister() {
	}

	public static void register(HttpServletResponse response) throws IOException {
		if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
			register(response, "Access denied!", response.getStatus(), new HashMap<>());
		}
	}

	public static void register(HttpServletResponse response, String message, int status, Map<String, Object> params) throws IOException {
		message = message == null ? "Unexpected Error" : message;
		HashMap<String, Object> responseParams = new HashMap<>();
		responseParams.put("message", message);
		responseParams.put("timestamp", new Date().getTime());
		responseParams.put("success", false);
		responseParams.put("code", status);
		response.setContentType("application/json;charset=UTF-8");
		responseParams.putAll(params);
		new ObjectMapper().writeValue(response.getOutputStream(), responseParams);
	}
}
