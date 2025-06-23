package com.midespensa.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.exceptions.EmailSendException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la logica de envio de correos electrónicos
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	@Value("${server.url.mail}")
	private String baseUrl;

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	/**
	 * Método envio mail al administrador de la aplicación
	 * 
	 * @param from    correo de origen
	 * @param subject asunto del correo
	 * @param body    cuerpo del correo
	 */
	@Override
	public void sendEmailFrom(String from, String suject, String body) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo("midespensa.contact@gmail.com");
		mensaje.setSubject(suject);
		mensaje.setText(body);
		mensaje.setFrom(from);

		// Envío del correo
		mailSender.send(mensaje);
	}

	/**
	 * Método envio correo validación cuenta de correo
	 * 
	 * @param to    correo del destinatario
	 * @param token único para seguridad adjunto como parámetro en la url de acceso
	 */
	@Override
	public void sendMailHtmlValidMail(String to, String token) {
		String resetUrl = baseUrl + "/user/valid-mail-form?token=" + token;
		// Crear un contexto para Thymeleaf
		Context context = new Context();
		// Agregar la url al contexto
		context.setVariable("resetUrl", resetUrl);
		// Procesar la plantilla Thymeleaf
		String htmlContenido = templateEngine.process("mails/valid-mail", context);
		sendMailHtml(to, "Validación correo - Mi despensa", htmlContenido);
	}

	/**
	 * Método envio correo restablecimiento contraseña
	 * 
	 * @param to    correo del destinatario
	 * @param token único para seguridad adjunto como parámetro en la url de acceso
	 */
	@Override
	public void sendMailHtmlResetPass(String to, String token) {
		String resetUrl = baseUrl + "/user/reset-password-form?token=" + token;
		// Carga plantilla correo
		Context context = new Context();
		context.setVariable("resetUrl", resetUrl);
		String htmlContenido = templateEngine.process("mails/reset-password", context);
		// Envio correo
		sendMailHtml(to, "Restablecimiento contraseña - Mi despensa", htmlContenido);
	}

	/**
	 * Método envío correo lista de la compra
	 * 
	 * @param to             correo del destinatario
	 * @param productDTOList lista de la compra
	 */
	@Override
	public void sendMailHtmlShoppingList(String to, List<ProductDTO> productDTOList) {
		// Crear un contexto para Thymeleaf
		Context context = new Context();
		// Agregar la lista al contexto
		context.setVariable("productDTOs", productDTOList);
		// Procesar la plantilla Thymeleaf
		String htmlContenido = templateEngine.process("mails/shoppingList", context);
		// Envio correo
		sendMailHtml(to, "Lista de la compra - Mi despensa", htmlContenido);

	}

	/**
	 * Método privado envío correos con html incrustado
	 * 
	 * @param to      correo del destinatario
	 * @param subject asunto del correo
	 * @param body    cuerpo del correo
	 */
	private void sendMailHtml(String to, String subject, String htmlContenido) {
		try {
			MimeMessage mensaje = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContenido, true);
			// Envio correo
			mailSender.send(mensaje);
		} catch (MessagingException e) {
			throw new EmailSendException("Error al componer el correo");
		} catch (MailException e) {
			throw new EmailSendException("Error al enviar el correo");
		}
	}

}
