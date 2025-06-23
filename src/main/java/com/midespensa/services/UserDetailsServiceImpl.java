package com.midespensa.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.midespensa.entities.User;
import com.midespensa.exceptions.UserException;
import com.midespensa.repositories.UsersRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de chequeo cuenta validada en Spring
 * Security
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		// Buscamos el usuario en la BD
		Optional<User> user = usersRepository.findByEmail(username);
		if (user.isEmpty()) {
			// Notíficamos que el usuario no existe
			throw new UserException("El usuario no registrado.");
		}
		if (user.isPresent() && !user.get().isValidatedEmail()) {
			// Notificamos que la cuenta no está validada
			throw new UserException("Tu cuenta no está validada. "
					+ "Revisa tu correo electrónico para confirmar tu cuenta. Si no lo encuentras, revisa la carpeta de spam.");
		}
		return (UserDetails) user.get();
	}

}
