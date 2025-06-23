package com.midespensa.dtos;

import java.util.HashSet;
import java.util.Set;

import com.midespensa.entities.Difficulty;
import com.midespensa.entities.RecipeCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
	private int id;

	private int idUser;

	private String title;

	private Difficulty difficulty;

	private RecipeCategory category;

	private String elaboration;

	private int prepTime;

	private int cookTime;

	private boolean shared;

	private String image;

	private Set<String> tagNames = new HashSet<>();

	private Set<String> ingredientNames = new HashSet<>();
}
