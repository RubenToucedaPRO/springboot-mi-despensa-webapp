package com.midespensa.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.midespensa.dtos.UserRegisterDTO;
import com.midespensa.dtos.UserViewDTO;
import com.midespensa.entities.User;
import com.midespensa.mappers.UserMapper;
import com.midespensa.repositories.UsersRepository;
import com.midespensa.services.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Controlador encargado de manejar las rutas de login de usuarios
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UsersService usersService;
	private final UsersRepository usersRepository;
	private final UserMapper userMapper;

	/**
	 * Muestra el formulario de login.
	 *
	 * @param error Objeto String usado para mostrar los errores de login en a la
	 *              vista Thymeleaf cuando no son correctos los datos
	 * @param model Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/login-form")
	public String showLoginForm(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", URLDecoder.decode(error, StandardCharsets.UTF_8));
		}
		return "user/login-form";
	}

	/**
	 * Cerrar la sesión del usuario , invalidando la sesión HTTP y limpiando el
	 * contexto de seguridad.
	 *
	 * @param request Objeto HttpServletRequest para acceder a la sesión actual
	 * @return Redirección a la página principal ("/")
	 */
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextHolder.clearContext(); // Borra la sesión del usuario
		request.getSession().invalidate(); // Invalida la sesión HTTP
		return "redirect:/";
	}

	/**
	 * Muestra el formulario registro.
	 *
	 * @param model Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/register-form")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new UserRegisterDTO());
		return "user/register-form";
	}

	/**
	 * Validación formulario de registro
	 *
	 * @param user               Usuario autenticado obtenido del
	 *                           AuthenticationPrincipal
	 * @param model              Objeto Model usado para pasar datos a la vista
	 *                           Thymeleaf
	 * @param redirectAttributes redirigir los mensajes de operacion existosa a la
	 *                           vista
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/register")
	public String register(@ModelAttribute UserRegisterDTO user, Model model, RedirectAttributes redirectAttributes) {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			model.addAttribute("error", "Las contraseñas no coinciden.");
			return "user/register-form";
		}
		if (!usersService.registerUser(user)) {
			model.addAttribute("error", "El correo electrónico ya está en uso.");
			return "user/register-form";
		}
		redirectAttributes.addFlashAttribute("success",
				"Hemos enviado un correo para verificar su cuenta. Revise su bandeja de entrada.");
		return "redirect:/user/login-form";
	}

	/**
	 * Muestra el formulario para validar el correo electrónico
	 *
	 * @param String             del token recibido
	 * @param model              Objeto Model usado para pasar datos a la vista
	 *                           Thymeleaf
	 * @param redirectAttributes redirigir el mensaje de token no valido a la vista
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/valid-mail-form")
	public String showValidMailForm(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {

		if (usersService.isTokenExpired(token)) {
			usersRepository.deleteByToken(token);
			redirectAttributes.addFlashAttribute("error",
					"El enlace de validación no es válido o ha expirado. Realice de nuevo el registro");
			return "redirect:/user/login-form";
		}
		model.addAttribute("token", token);
		return "user/valid-mail-form";
	}

	/**
	 * Validacion formulario confirmación correo electrónico valido
	 *
	 * @param token              token para busqueda del usuario por dicho token
	 * @param redirectAttributes redirigir los mensajes de operacion existosa a la
	 *                           vista
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/valid-mail")
	public String processValidMail(@RequestParam String token, RedirectAttributes redirectAttributes) {
		if (!usersService.validateEmail(token)) {
			redirectAttributes.addFlashAttribute("error", "El enlace de validación correo no es válido o ha expirado.");
			return "redirect:/user/login-form";
		}
		redirectAttributes.addFlashAttribute("success",
				"Tu correo ha sido validado con éxito. ¡Bienvenido a Mi Despensa! Ya puedes iniciar sesión.");
		return "redirect:/user/login-form";
	}

	/**
	 * Muestra el formulario para introducir el email al que enviar el enlace de
	 * recuperacion de contraseña
	 *
	 * @param model Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/forgot-password-form")
	public String showForgotPasswordForm(Model model) {
		return "user/forgot-password-form";
	}

	/**
	 * Validar formulario enviar email con el enlace de recuperacion de contraseña
	 *
	 * @param userEmail          correo del usuario
	 * @param redirectAttributes redirigir los mensajes de operacion existosa a la
	 *                           vista
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam("email") String userEmail,
			RedirectAttributes redirectAttributes) {
		String message = usersService.processForgotPassword(userEmail);
		redirectAttributes.addFlashAttribute("success", message);
		return "redirect:/user/forgot-password-form";
	}

	/**
	 * Muestra el formulario para introducir la nueva contraseña
	 *
	 * @param String             del token recibido
	 * @param model              Objeto Model usado para pasar datos a la vista
	 *                           Thymeleaf
	 * @param redirectAttributes redirigir el mensaje de token no valido a la vista
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/reset-password-form")
	public String showResetPasswordForm(@RequestParam String token, Model model,
			RedirectAttributes redirectAttributes) {
		
		if (usersService.isTokenExpired(token)) {
			redirectAttributes.addFlashAttribute("error", "El enlace de restablecimiento no es válido o ha expirado.");
			return "redirect:/user/login-form";
		}
		model.addAttribute("token", token);
		return "user/reset-password-form";
	}

	/**
	 * Validar formulario cambio de contraseña
	 *
	 * @param token              token para busqueda del usuario por dicho token
	 * @param newPassword        String nueva contraseña
	 * @param confirmPassword    String confirmación contraseña
	 * @param redirectAttributes redirigir los mensajes de operacion existosa a la
	 *                           vista
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/reset-password")
	public String processResetPassword(@RequestParam String token, @RequestParam("password") String newPassword,
			@RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {

		Optional<User> userOptional = usersService.findUserByToken(token);

		if (userOptional.isEmpty() || usersService.isTokenExpired(token)) {
			redirectAttributes.addFlashAttribute("error", "El enlace de restablecimiento no es válido o ha expirado.");
			return "redirect:/user/login-form";
		}

		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
			redirectAttributes.addFlashAttribute("token", token);
			return "redirect:/user/reset-password-form?token=" + token; // Vuelve al formulario de reseteo
		}

		User user = userOptional.get();
		user.setPassword(newPassword);
		user.setToken(null);
		user.setTokenExpiryDate(null);
		usersService.saveUser(user, true, false);
		redirectAttributes.addFlashAttribute("success",
				"Tu contraseña ha sido restablecida con éxito. Por favor, inicie sesión.");
		return "redirect:/user/login-form";
	}

	/**
	 * Muestra el formulario de ajustes de usuario
	 *
	 * @param model Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param user  Usuario autenticado obtenido del AuthenticationPrincipal
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/settings-form")
	public String showSettingsForm(Model model, @AuthenticationPrincipal User user) {
		UserRegisterDTO dto = userMapper.toUserRegisterDto(user);
		model.addAttribute("user", dto);
		return "user/settings-form";
	}

	/**
	 * Validar formulario cambios de ajustes de usuario
	 *
	 * @param user               Usuario autenticado obtenido del
	 *                           AuthenticationPrincipal
	 * @param redirectAttributes redirigir los mensajes de operacion existosa a la
	 *                           vista
	 * @param model              Objeto Model usado para pasar datos a la vista
	 *                           Thymeleaf
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/update")
	public String processChangeSettings(@ModelAttribute UserRegisterDTO user, RedirectAttributes redirectAttributes,
			Model model) {

		if (!user.getPassword().equals(user.getConfirmPassword())) {
			redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
			return "redirect:/user/settings-form";
		}
		user.setPassword(user.getPassword());

		String request = usersService.updateUser(userMapper.fromRegisterDtotoUser(user));
		if (request.equals("ok")) {
			redirectAttributes.addFlashAttribute("success", "Cambios guardados con exito");
			return "redirect:login-form";
		}

		redirectAttributes.addFlashAttribute("error", request);
		return "redirect:/user/settings-form";
	}

	//

	/**
	 * Validar solicitud baja usuario
	 *
	 * @param user Usuario autenticado obtenido del AuthenticationPrincipal
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("unsubscribe")
	public String unsubscribe(@AuthenticationPrincipal User user) {
		usersService.deleteUserById(user.getId());
		return "redirect:/";
	}

	/*
	 * *************************************************************************
	 * Gestion usuarios admin
	 * *************************************************************************
	 */

	/**
	 * Muestra la página de la lista de usuarios autenticados
	 *
	 * @param id   entero usado para pasar id del producto desde la vista Thymeleaf
	 * @param user Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page Número de página para la paginación
	 * @param size Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@GetMapping("/users")
	public String findAll(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "11") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<UserViewDTO> users = usersService.findAll(pageable);
		model.addAttribute("users", users);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", users.getTotalPages());
		model.addAttribute("size", size);
		return "user/users-list";
	}

	/**
	 * Borrar usuario por parte del admin
	 *
	 * @param id   entero usado para pasar id del producto a borrar desde la vista
	 *             Thymeleaf
	 * @param user Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page Número de página para la paginación
	 * @param size Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/{id}/delete")
	public String deleteUserAdmin(@PathVariable int id, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "11") int size) {
		usersService.deleteUserById(id);
		return "redirect:/user/users?page=" + page + "&size=" + size;
	}

}
