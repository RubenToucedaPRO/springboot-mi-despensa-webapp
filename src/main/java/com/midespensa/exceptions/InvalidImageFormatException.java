package com.midespensa.exceptions;

public class InvalidImageFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidImageFormatException(String message) {
		super(message);
	}

	public InvalidImageFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
