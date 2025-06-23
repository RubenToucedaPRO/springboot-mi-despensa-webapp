package com.midespensa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.midespensa.dtos.UserRegisterDTO;
import com.midespensa.dtos.UserViewDTO;
import com.midespensa.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "confirmPassword", ignore = true)
	UserRegisterDTO toUserRegisterDto(User user);
	
	@Mapping(target = "lastNotification", ignore = true)
	@Mapping(target = "lastConnection", ignore = true)
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "token", ignore = true)
	@Mapping(target = "tokenExpiryDate", ignore = true)
	@Mapping(target = "validatedEmail", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	User fromRegisterDtotoUser(UserRegisterDTO user);
	
	UserViewDTO toUserViewDto(User user);

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "confirmPassword", ignore = true)
	@Mapping(target = "token", ignore = true)
	@Mapping(target = "tokenExpiryDate", ignore = true)
	@Mapping(target = "validatedEmail", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	User fromUserViewtoUser(UserViewDTO userDTO);

}