package com.midespensa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midespensa.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {

	Optional<Tag> findByName(String name); // Buscar un tag por su nombre

	// Buscar un tag por su nombre (ignorando mayúsculas/minúsculas)
	List<Tag> findByNameContainingIgnoreCase(String name);

}
