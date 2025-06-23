package com.midespensa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.midespensa.entities.RecipeCategory;
import com.midespensa.repositories.RecipeCategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de obtener la lista de categorías
 * recetas
 */
@Service
@RequiredArgsConstructor
public class RecipeCategoryServiceImpl implements RecipeCategoryService {

	private final RecipeCategoryRepository recipeCategoryRepository;

	/**
	 * Método obtener todas las categorías en base de datos
	 * 
	 * @return Lista de categorías
	 */
	@Override
	public List<RecipeCategory> findAll() {
		return recipeCategoryRepository.findAll();
	}

}
