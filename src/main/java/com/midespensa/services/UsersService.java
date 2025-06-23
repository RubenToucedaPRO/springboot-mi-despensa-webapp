package com.midespensa.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.midespensa.dtos.UserRegisterDTO;
import com.midespensa.dtos.UserViewDTO;
import com.midespensa.entities.User;

public interface UsersService {

	Page<UserViewDTO> findAll(Pageable pageable);

	boolean registerUser(UserRegisterDTO userRDto);

	void saveUser(User user, boolean encodePass, boolean sendEmail);

	String processForgotPassword(String userEmail);
	
	boolean validateEmail(String token);

	boolean isTokenExpired(String token);

	Optional<User> findUserByToken(String token);

	String updateUser(User user);

	void deleteUserById(int id);
}
