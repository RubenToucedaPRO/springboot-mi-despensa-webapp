package com.midespensa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.NoResultException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(RuntimeException ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(NoResultException.class)
	public String handleRuntimeException(NoResultException ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResourceFoundException(NoResourceFoundException ex, Model model) {
		// ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public String handleProductNotFound(ProductNotFoundException ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(ImageUploadException.class)
	public String handleImageUploadException(ImageUploadException ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(InvalidImageFormatException.class)
	public ResponseEntity<String> handleInvalidImageFormatException(InvalidImageFormatException ex, Model model) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error: " + ex.getMessage());
	}

	@ExceptionHandler(EmailSendException.class)
	public String handleEmailSendException(EmailSendException ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(OpenFoodFactsApiException.class)
	public String handleOpenFoodFactsApiException(OpenFoodFactsApiException ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<String> handleUserException(UserException ex, Model model) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocurrió un error: " + ex.getMessage());
	}

//Excepcion general, siempre de ultimo
	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "Ocurrió un error: " + ex.getMessage());
		return "error";
	}
}
