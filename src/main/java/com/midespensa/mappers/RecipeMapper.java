package com.midespensa.mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.midespensa.dtos.RecipeDTO;
import com.midespensa.entities.Ingredient;
import com.midespensa.entities.Recipe;
import com.midespensa.entities.Tag;
import com.midespensa.repositories.IngredientRepository;
import com.midespensa.repositories.TagRepository;

@Mapper(componentModel = "spring")
public abstract class RecipeMapper {

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private TagRepository tagRepository;

	@Mapping(target = "ingredientNames", source = "ingredients", qualifiedByName = "mapIngredientsToNames")
	@Mapping(target = "tagNames", source = "tags", qualifiedByName = "mapTagsToNames")
	public abstract RecipeDTO toDto(Recipe recipe);

	@Mapping(target = "ingredients", source = "ingredientNames", qualifiedByName = "mapNamesToIngredients")
	@Mapping(target = "tags", source = "tagNames", qualifiedByName = "mapNamesToTags")
	@Mapping(target = "dateUpdate", ignore = true)
	public abstract Recipe toRecipe(RecipeDTO recipeDTO);

	@Named("mapIngredientsToNames")
	protected Set<String> mapIngredientsToNames(Set<Ingredient> ingredients) {
		if (ingredients == null || ingredients.isEmpty()) {
			return new HashSet<>();
		}
		return ingredients.stream().map(Ingredient::getName).collect(Collectors.toSet());
	}

	@Named("mapNamesToIngredients")
	protected Set<Ingredient> mapNamesToIngredients(Set<String> ingredientNames) {
		if (ingredientNames == null || ingredientNames.isEmpty()) {
			return new HashSet<>();
		}
		Set<Ingredient> ingredients = new HashSet<>();
		ingredientNames.stream().filter(ingredientname -> !ingredientname.isEmpty()).map(this::findOrCreateIngredient)
				.forEach(ingredients::add);
		return ingredients;
	}

	// Al guardar receta verifica si existe el ingrediente en el repositorio y si no
	// lo añade
	protected Ingredient findOrCreateIngredient(String ingredientName) {
		String finalIngredientName = ingredientName;
		return ingredientRepository.findByName(ingredientName).orElseGet(() -> {
			Ingredient ingredient = new Ingredient();
			ingredient.setName(finalIngredientName);
			return ingredientRepository.save(ingredient);
		});
	}

	@Named("mapTagsToNames")
	protected Set<String> mapTagsToNames(Set<Tag> tags) {
		if (tags == null || tags.isEmpty()) {
			return new HashSet<>();
		}
		return tags.stream().map(Tag::getName).collect(Collectors.toSet());
	}

	@Named("mapNamesToTags")
	protected Set<Tag> mapNamesToTags(Set<String> tagNames) {
		if (tagNames == null || tagNames.isEmpty()) {
			return new HashSet<>();
		}
		Set<Tag> tags = new HashSet<>();
		tagNames.stream().filter(tagname -> !tagname.isEmpty()).map(this::findOrCreateTag).forEach(tags::add);
		return tags;
	}

	// Al guardar receta verifica si existe el tag en el repositorio y si no lo
	// añade
	protected Tag findOrCreateTag(String tagName) {
		String finalTagName = tagName;
		return tagRepository.findByName(tagName).orElseGet(() -> {
			Tag tag = new Tag();
			tag.setName(finalTagName);
			return tagRepository.save(tag);
		});
	}
}