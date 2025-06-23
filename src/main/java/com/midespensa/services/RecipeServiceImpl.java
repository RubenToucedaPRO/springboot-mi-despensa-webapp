package com.midespensa.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.midespensa.entities.Ingredient;
import com.midespensa.entities.Recipe;
import com.midespensa.entities.Tag;
import com.midespensa.repositories.RecipeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de manejar las recetas
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;

	private final IngredientService ingredientService;

	private final TagService tagService;

	private final CloudinaryService cloudinaryService;

	@Override
	public List<Recipe> findFilter(Integer difficultyId, Integer categoryId, List<String> ingredientNames,
			boolean exact, int idUser) {
		int n = 0;
		if (ingredientNames != null && ingredientNames.size() > 0) {
			n = 1;
			if (exact) {
				n = ingredientNames.size();
			}
		}
		if (difficultyId == null) {
			difficultyId = 0;
		}
		if (categoryId == null) {
			categoryId = 0;
		}
		if (difficultyId > 0 && categoryId > 0 && n > 0) {
			return recipeRepository.findByDifficultyIdAndCategoryIdAndIdUserOrShared(difficultyId, categoryId, idUser,
					true, ingredientNames, n);
		} else if (difficultyId > 0 && categoryId > 0) {
			return recipeRepository.findByDifficultyIdAndCategoryIdAndIdUserOrShared(difficultyId, categoryId, idUser,
					true);
		} else if (difficultyId > 0 && n > 0) {
			return recipeRepository.findByDifficultyIdAndIdUserOrShared(difficultyId, idUser, true, ingredientNames, n);
		} else if (categoryId > 0 && n > 0) {
			return recipeRepository.findByCategoryIdAndIdUserOrShared(categoryId, idUser, true, ingredientNames, n);
		} else if (difficultyId > 0) {
			return recipeRepository.findByDifficultyIdAndIdUserOrShared(difficultyId, idUser, true);
		} else if (categoryId > 0) {
			return recipeRepository.findByCategoryIdAndIdUserOrShared(categoryId, idUser, true);
		} else if (n > 0) {
			return recipeRepository.findByIdUserOrShared(idUser, true, ingredientNames, n);
		}
		return recipeRepository.findByIdUserOrShared(idUser, true);
	}

	@Override
	public List<Recipe> findFilterAdmin(Integer difficultyId, Integer categoryId, List<String> ingredientNames,
			boolean exact) {
		int n = 0;
		if (ingredientNames != null && ingredientNames.size() > 0) {
			n = 1;
			if (exact) {
				n = ingredientNames.size();
			}
		}
		if (difficultyId == null) {
			difficultyId = 0;
		}
		if (categoryId == null) {
			categoryId = 0;
		}
		if (difficultyId > 0 && categoryId > 0 && n > 0) {
			return recipeRepository.findByDifficultyIdAndCategoryIdAndIngredients(difficultyId, categoryId,
					ingredientNames, n);
		} else if (difficultyId > 0 && categoryId > 0) {
			return recipeRepository.findByDifficultyIdAndCategoryId(difficultyId, categoryId);
		} else if (difficultyId > 0 && n > 0) {
			return recipeRepository.findByDifficultyIdAndIngredients(difficultyId, ingredientNames, n);
		} else if (categoryId > 0 && n > 0) {
			return recipeRepository.findByCategoryIdAndIngredients(categoryId, ingredientNames, n);
		} else if (difficultyId > 0) {
			return recipeRepository.findByDifficultyId(difficultyId);
		} else if (categoryId > 0) {
			return recipeRepository.findByCategoryId(categoryId);
		} else if (n > 0) {
			return recipeRepository.findByIngredients(ingredientNames, n);
		}
		return recipeRepository.findAll();
	}

	@Override
	public Recipe getById(int id) {
		return recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id no valido:" + id));
	}

	@Override
	public void save(Recipe recipe, int idUser, MultipartFile imageFile) {
		// Asignar el ID del usuario a la receta
		recipe.setIdUser(idUser);

		// Asociar ingredientes a la receta
		Set<Ingredient> currentIngredients = recipe.getIngredients();
		Set<Ingredient> processedIngredients = new HashSet<>();
		for (Ingredient ingredient : currentIngredients) {
			Ingredient processedIngredient = ingredientService.findOrCreate(ingredient.getName());
			processedIngredients.add(processedIngredient);
		}
		recipe.setIngredients(processedIngredients);

		// Asociar tags a la receta
		Set<Tag> currentTags = recipe.getTags();
		Set<Tag> processedTags = new HashSet<>();
		for (Tag tag : currentTags) {
			Tag processedTag = tagService.findOrCreate(tag.getName());
			processedTags.add(processedTag);
		}
		recipe.setTags(processedTags);

		@SuppressWarnings("rawtypes")
		Map map;
		// Procesar la imagen si se ha subido un archivo
		if (!imageFile.isEmpty()) {
			// Eliminar la imagen anterior si existe
			if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
				cloudinaryService.delete(recipe.getImage());
			}
			map = cloudinaryService.uploadFile(imageFile);
			// Asignar la ruta de la imagen
			recipe.setImage((String) map.get("public_id"));
		}
		recipe.setDateUpdate(LocalDate.now());

		recipeRepository.save(recipe);
	}

	@Override
	public void delete(int id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);
		if (recipe.isPresent()) {
			String imageName = recipe.get().getImage();
			if (imageName != null && !imageName.isEmpty()) {
				// imageStorageService.delete(imageName, "recipes/");
				cloudinaryService.delete(imageName);
			}
			recipeRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Receta no encontrada con ID: " + id);
		}
	}

}
