package com.midespensa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midespensa.entities.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

	Optional<Ingredient> findByName(String name); // Buscar un tag por su nombre

	// Buscar un ingrediente por su nombre (ignorando mayúsculas/minúsculas)
	List<Ingredient> findByNameContainingIgnoreCase(String name);
}
