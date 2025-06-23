package com.midespensa.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public UserException(String message) {
		super(message);
	}

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}
}