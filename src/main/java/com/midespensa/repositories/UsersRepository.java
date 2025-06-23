package com.midespensa.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String username);

	boolean existsByEmail(String email);

	Optional<User> findByToken(String token);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.lastConnection = :lastConnection WHERE u.id = :id")
	void updateLastLogin(@Param("id") int id, @Param("lastConnection") LocalDate last_connection);

	@Modifying
	@Transactional
	void deleteByToken(String token);
}
