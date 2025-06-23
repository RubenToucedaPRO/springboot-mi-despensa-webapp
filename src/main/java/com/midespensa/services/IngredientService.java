package com.midespensa.services;

import java.util.List;

import com.midespensa.entities.Ingredient;

public interface IngredientService {
	// Busca un ingrediente por nombre o lo crea si no existe
	Ingredient findOrCreate(String Ingredient);

	// Devuelve una lista de nombres de ingredientes que contienen los caracteres
	// introducidos por el usuario
	List<String> findIngredientsByNameContaining(String chars);

}
