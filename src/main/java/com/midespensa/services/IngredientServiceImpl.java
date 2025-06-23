package com.midespensa.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.midespensa.entities.Ingredient;
import com.midespensa.repositories.IngredientRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de obtener la lista de ingredientes
 */
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

	private final IngredientRepository ingredientRepository;

	/**
	 * Método obtener y crear ingredientes en BD en base de datos
	 * 
	 * @param ingredientName nombre del tag insertado por el usuario
	 * @return ingrediente creado u obtenido
	 */
	@Override
	public Ingredient findOrCreate(String ingredientName) {
		return ingredientRepository.findByName(ingredientName).orElseGet(() -> {
			Ingredient newIngredient = new Ingredient();
			newIngredient.setName(ingredientName);
			return ingredientRepository.save(newIngredient);
		});
	}

	/**
	 * Método obtener lista de ingredientes de la base de datos en función de
	 * caracteres introducidos
	 * 
	 * @param chars caracteres introducidos por el usuario que deben contener los
	 *              ingredientes solicitados a BD
	 * @return lista de ingredientes obtenidos que contienen esos caracteres
	 */
	@Override
	public List<String> findIngredientsByNameContaining(String chars) {
		return ingredientRepository.findByNameContainingIgnoreCase(chars).stream().map(Ingredient::getName)
				.collect(Collectors.toList());
	}
}
