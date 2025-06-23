package com.midespensa.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.midespensa.entities.Recipe;

public interface RecipeService {

	List<Recipe> findFilter(Integer difficultyId, Integer categoryId, List<String> ingredients, boolean exact,
			int idUser);

	List<Recipe> findFilterAdmin(Integer difficultyId, Integer categoryId, List<String> ingredients, boolean exact);

	Recipe getById(int id);

	void save(Recipe recipe, int idUser, MultipartFile imageFile);

	void delete(int id);
}
