package com.midespensa.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.midespensa.dtos.UserRegisterDTO;
import com.midespensa.dtos.UserViewDTO;
import com.midespensa.entities.User;
import com.midespensa.exceptions.EmailSendException;
import com.midespensa.exceptions.UserException;
import com.midespensa.mappers.UserMapper;
import com.midespensa.repositories.UsersRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de gestión de usuarios
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final UserMapper userMapper;

	/**
	 * Método para listar usuarios
	 * 
	 * @param pageable parametros de paginación
	 * @return usuarios paginados
	 */
	@Override
	public Page<UserViewDTO> findAll(Pageable pageable) {
		Page<User> list =usersRepository.findAll(pageable);
		return list.map(userMapper::toUserViewDto);
	}

	/**
	 * Método para registro de usuario
	 * 
	 * @param userRDto datos de usuario
	 * @return boolean de si fué registrado o no con éxito
	 */
	@Override
	public boolean registerUser(UserRegisterDTO userRDto) {
		// Comprobamos que no existe un usuario con ese email en BD
		Optional<User> userExistOp = usersRepository.findByEmail(userRDto.getEmail());
		if (userExistOp.isPresent()) {
			// Verificamos si la cuenta fué validada porque si no lo fue permitiremos el
			// registro del nuevo usuario
			if (userExistOp.get().isValidatedEmail()
					|| userExistOp.get().getTokenExpiryDate().isAfter(LocalDateTime.now())) {
				// retornamos que no se puede realizar el registro
				return false;
			}
			// Si existe y la cuenta no validada cogemos su id para sobreescribir los datos
			userRDto.setId(userExistOp.get().getId());
		}
		// Llamamos al método de guardar usuario en BD
		saveUser(userMapper.fromRegisterDtotoUser(userRDto), true, true);
		// Retornamos que fué exitoso el registro
		return true;
	}

	/**
	 * Método para guardar el usuario en BD
	 * 
	 * @param user       datos de usuario
	 * @param encodePass booleano para indicar si es necesario cifrar la contraseña
	 * @param sendEmail  booleano para indicar si es necesario enviar email de
	 *                   validación cuenta
	 */
	@Override
	public void saveUser(User user, boolean encodePass, boolean sendEmail) {
		// Creamos un token aleatorio
		String token = UUID.randomUUID().toString();
		// Ciframos la clave si es indicado
		if (encodePass) {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
		}
		// Mdificamos campos de validacion cuenta si es necesario enviar email
		if (sendEmail) {
			user.setToken(token);
			user.setRole("USER");
			user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
			user.setValidatedEmail(false);
		}

		// Guardar el usuario en BD
		usersRepository.save(user);

		// Enviar correo de verificación si corresponde
		if (sendEmail) {
			emailService.sendMailHtmlValidMail(user.getEmail(), token);
		}
	}

	/**
	 * Método para procesar la soliciud de restablecimiento de contraseña
	 * 
	 * @param user Email correo del usuario
	 * @return mensaje de respuesta a la solicitud de reestablecimiento
	 */
	@Override
	public String processForgotPassword(String userEmail) {
		// Buscamos el usuario por email
		Optional<User> userOptional = usersRepository.findByEmail(userEmail);
		// Si existe
		if (userOptional.isPresent()) {
			// Obtenemos sus datos
			User user = userOptional.get();
			// Generamos un token
			String token = UUID.randomUUID().toString();
			// Establecemos la fecha de expiracion del correo 30 minutos mas tarde de la
			// solicitud
			LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);
			// Actualizamos los datos del usuario los guardamos en BD
			user.setToken(token);
			user.setTokenExpiryDate(expiryDate);
			usersRepository.save(user);
			try {
				// Enviamos email de reestablecimiento contraseña
				emailService.sendMailHtmlResetPass(user.getEmail(), token);
				return "Se ha enviado un enlace para restablecer la contraseña a tu correo electrónico.";
			} catch (Exception e) {
				throw new EmailSendException("Error al enviar el correo de restablecimiento");
			}
		}
		// Si no existe usuario con ese email enviamos mensaje de aviso
		return "Si tu dirección de correo electrónico está registrada, recibirás un enlace para restablecer la contraseña.";
	}
	
	/**
	 * Método para validacion del correo
	 * 
	 * @param token token a chequear
	 * @return boolean si ha expirado o no
	 */
	@Override
	public boolean validateEmail(String token) {
		Optional<User> userOptional = findUserByToken(token);
		if(userOptional.isEmpty() || isTokenExpired(token)) {
			return false;
		}
		User user = userOptional.get();
		user.setToken(null);
		user.setTokenExpiryDate(null);
		user.setValidatedEmail(true);
		saveUser(user, false, false);
		return true;
	}

	/**
	 * Método para comprobar si el token ha expirado
	 * 
	 * @param token token a chequear
	 * @return boolean si ha expirado o no
	 */
	@Override
	public boolean isTokenExpired(String token) {
		// Buscamos el usuario por token
		Optional<User> userOptional = usersRepository.findByToken(token);
		if (userOptional.isPresent()) {
			// Si existe comprobamos fecha de expiración
			LocalDateTime expiryDate = userOptional.get().getTokenExpiryDate();
			// Si no existe o no expiró respondemos con false
			return expiryDate == null || expiryDate.isBefore(LocalDateTime.now());
		}
		// En caso contrario se responde que si expiró
		return true;
	}

	/**
	 * Método para buscar usuario por token
	 * 
	 * @param token token a buscar
	 * @return usuario con ese token
	 */
	@Override
	public Optional<User> findUserByToken(String token) {
		return usersRepository.findByToken(token);
	}

	/**
	 * Método para actualizar datos de usuario
	 * 
	 * @param user usuario a actualizar
	 * @return mensje de resultado de la actualizacion
	 */
	@Override
	public String updateUser(User userNew) {
		boolean sendEmail = false;
		User userDb;
		try {
			// Buscamos el usuario por la id
			Optional<User> userOptional = usersRepository.findById(userNew.getId());
			if (userOptional.isPresent()) {
				// Si existe lo guardamos en una variable user
				userDb = userOptional.get();
				// Comprobamas que trae email y que ese email no está usado por otro usuario
				if (StringUtils.isNotBlank(userNew.getEmail())) {
					if (!userDb.getEmail().equals(userNew.getEmail())) {
						if (usersRepository.findByEmail(userNew.getEmail()).isPresent()) {
							return "Cuenta de correo en uso por otro usuario";
						}
						// Enviamos mail si el usuario cambió de correo
						sendEmail = true;
						userDb.setEmail(userNew.getEmail());
					}
				}
				// Si modificó la contraseña la actualizamos
				if (StringUtils.isNotBlank(userNew.getPassword())
						&& StringUtils.isNotBlank(userNew.getConfirmPassword())) {
					userDb.setPassword(userNew.getPassword());
				}
				// Con todos los datos actualizados los salvamos en BD
				saveUser(userDb, !userNew.getPassword().isBlank(), sendEmail);
				return "ok";
			}
			return "Fallo al guardar los datos";
		} catch (DataAccessException e) {
			throw new UserException("Error al acceder a la base de datos: " + e.getMessage());
		} catch (NullPointerException e) {
			throw new UserException("Error inesperado: " + e.getMessage());
		} catch (Exception e) {
			throw new UserException("Ocurrió un error inesperado: " + e.getMessage());
		}
	}

	/**
	 * Método para borrar usuario
	 * 
	 * @param id id del usuario a borrar
	 */
	@Override
	public void deleteUserById(int id) {
		usersRepository.deleteById(id);
	}
}
