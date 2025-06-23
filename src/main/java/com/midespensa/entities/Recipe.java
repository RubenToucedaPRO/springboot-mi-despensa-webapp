package com.midespensa.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "id_user")
	private int idUser;

	@ManyToOne
	@JoinColumn(name = "id_difficulty", nullable = false)
	private Difficulty difficulty;

	@ManyToOne
	@JoinColumn(name = "id_category", nullable = false)
	private RecipeCategory category;

	private boolean shared;

	private String title;

	private String elaboration;

	@Column(name = "prep_time")
	private int prepTime;

	@Column(name = "cook_time")
	private int cookTime;

	@ManyToMany
	@JoinTable(name = "recipe_tag", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "recipe_ingredient", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
	private Set<Ingredient> ingredients = new HashSet<>();

	@Column(name = "image")
	private String image;

	@Column(name = "date_update")
	private LocalDate dateUpdate;
}
