package com.midespensa.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserViewDTO {

	private int id;

	private String email;

	private LocalDate lastNotification;

	private LocalDate lastConnection;

	private String role;
}
