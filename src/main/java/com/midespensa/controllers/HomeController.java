package com.midespensa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.midespensa.services.EmailService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador encargado de manejar las rutas principales de la aplicación,
 * página de inicio, política de privacidad, contacto y envío de correos desde
 * el formulario de contacto.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final EmailService emailService;

	/**
	 * Muestra la página principal de la aplicación.
	 *
	 * @return El nombre de la vista "index"
	 */
	@GetMapping("/index")
	public String showHomePage() {
		return "index";
	}

	/**
	 * Muestra la página de política de privacidad.
	 *
	 * @return El nombre de la vista "privacy"
	 */
	@GetMapping("/privacy")
	public String showPrivacyPage() {
		return "privacy";
	}

	/**
	 * Muestra el formulario de contacto.
	 *
	 * @return El nombre de la vista "contact"
	 */
	@GetMapping("/contact")
	public String showContactPage() {
		return "contact";
	}

	/**
	 * Procesa los datos del formulario de contacto y envía correo electrónico al
	 * correo de la aplicación.
	 *
	 * @param name    Nombre del remitente proporcionado en el formulario.
	 * @param from    Dirección de correo del remitente.
	 * @param subject Asunto del mensaje.
	 * @param message Contenido del mensaje.
	 * @return Una redirección a la página de inicio.
	 */
	@PostMapping("/contact-send-from")
	public String processContact(@RequestParam String name, @RequestParam String from, @RequestParam String subject,
			@RequestParam String message, RedirectAttributes redirectAttributes) {
		emailService.sendEmailFrom(from, subject,
				"Soy ".concat(name).concat(". ").concat(message).concat("\n").concat(from));
		redirectAttributes.addFlashAttribute("success", "Tu mensaje ha sido enviado.");
		return "redirect:/";
	}
}
