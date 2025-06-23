package com.midespensa.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	@Column(name = "pass")
	private String password;

	@Transient
	private String confirmPassword;

	@Column(name = "last_notification")
	private LocalDate lastNotification;

	@Column(name = "last_connection")
	private LocalDate lastConnection;

	@Column(name = "role_")
	private String role;

	@Column(name = "token", unique = true) // unique = true es opcional
	private String token;

	@Column(name = "token_expiry_date")
	private LocalDateTime tokenExpiryDate;

	@Column(name = "validated_email")
	private boolean validatedEmail;

	// Implementación de métodos de UserDetails
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Convierte el rol en una autoridad de Spring Security
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
