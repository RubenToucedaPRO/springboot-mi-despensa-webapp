package com.midespensa.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

	private int id;

	private String email;
	
	private String password;

	private String confirmPassword;
	
}
