package com.midespensa.exceptions;

public class ProductUpdateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductUpdateException(String message) {
		super(message);
	}

	public ProductUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
}
