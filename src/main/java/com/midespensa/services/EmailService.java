package com.midespensa.services;

import java.util.List;

import com.midespensa.dtos.ProductDTO;

public interface EmailService {

	public void sendEmailFrom(String from, String subject, String body);

	public void sendMailHtmlValidMail(String to, String token);

	public void sendMailHtmlResetPass(String to, String token);

	public void sendMailHtmlShoppingList(String to, List<ProductDTO> productDtoList);
}
