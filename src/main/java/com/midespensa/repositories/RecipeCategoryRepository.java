package com.midespensa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.RecipeCategory;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Integer> {

}
