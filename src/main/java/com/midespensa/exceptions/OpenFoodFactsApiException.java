package com.midespensa.exceptions;

public class OpenFoodFactsApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OpenFoodFactsApiException(String message) {
		super(message);
	}

	public OpenFoodFactsApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
