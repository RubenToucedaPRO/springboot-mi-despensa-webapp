package com.midespensa.configs;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import com.midespensa.exceptions.UserException;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(configurer -> configurer

				// Recursos estáticos
				.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

				// Página principal
				.requestMatchers("/", "/privacy", "/contact", "index.html", "privacy.html", "contact.html").permitAll()

				// Formularios públicos (GET)
				.requestMatchers("/user/login-form", "/user/register-form", "/user/forgot-password-form",
						"/user/reset-password-form", "/user/valid-mail-form", "/contact-send-from")
				.permitAll()

				// Acciones públicas (POST)
				.requestMatchers(HttpMethod.POST, "/user/register", "/user/forgot-password", "/user/reset-password",
						"/user/valid-mail", "/contact-send-from")
				.permitAll()

				// Fragments y layouts requeridos para usuarios autenticados
				.requestMatchers("/fragments/**", "/layouts/**").authenticated()

				// Endpoints generales de usuario autenticado (GET)
				.requestMatchers(HttpMethod.GET, "/products/view", "/pantry/**", "/recipe/**", "/shoppingList/**",
						"/user/settings-form")
				.authenticated()

				// Endpoints generales de usuario autenticado (POST)
				.requestMatchers(HttpMethod.POST, "/products/view", "/products/scanner", "/pantry/**", "/recipe/**",
						"/shoppingList/**", "/user/update")
				.authenticated()

				// Endpoints administrativos (GET)
				.requestMatchers(HttpMethod.GET, "/products/**", "/user/users").hasAuthority("ADMIN")

				// Endpoints administrativos (POST)
				.requestMatchers(HttpMethod.POST, "/products/**", "/user/users").hasAuthority("ADMIN")

				// Cualquier otra petición
				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/user/login-form").loginProcessingUrl("/user/login")
						.defaultSuccessUrl("/pantry/list", true).permitAll()
						.failureHandler(customAuthenticationFailureHandler()))
				.logout(logout -> logout.logoutUrl("/logout") // URL para activar el logout
						.logoutSuccessUrl("/") // <-- Redirige a la raíz en lugar de la página por defecto
						.invalidateHttpSession(true) // Invalida la sesión HTTP
						.deleteCookies("JSESSIONID") // Elimina cookies específicas
						.permitAll());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return (request, response, exception) -> {
			String errorMessage = "Usuario o contraseña incorrectos";

			if (exception instanceof InternalAuthenticationServiceException) {
				errorMessage = exception.getMessage();
			}
			if (exception instanceof UserException) {
				errorMessage = exception.getMessage();
			}

			response.sendRedirect("/user/login-form?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
		};
	}
}
