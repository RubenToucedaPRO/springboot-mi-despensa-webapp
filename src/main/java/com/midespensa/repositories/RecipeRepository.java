package com.midespensa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

	@Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE (r.difficulty.id = :difficultyId) AND (r.idUser = :idUser OR r.shared = :shared)"
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByDifficultyIdAndIdUserOrShared(Integer difficultyId, int idUser, boolean shared,
			List<String> ingredientNames, int ingredientCount);

	@Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE (r.category.id = :categoryId) AND (r.idUser = :idUser OR r.shared = :shared)"
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByCategoryIdAndIdUserOrShared(int categoryId, int idUser, boolean shared,
			List<String> ingredientNames, int ingredientCount);

	@Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE (r.difficulty.id = :difficultyId AND r.category.id = :categoryId) AND (r.idUser = :idUser OR r.shared = :shared)"
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByDifficultyIdAndCategoryIdAndIdUserOrShared(Integer difficultyId, Integer categoryId, int idUser,
			boolean shared, List<String> ingredientNames, int ingredientCount);

	@Query("SELECT r FROM Recipe r WHERE (r.difficulty.id = :difficultyId AND r.category.id = :categoryId) AND (r.idUser = :idUser OR r.shared = :shared)")
	List<Recipe> findByDifficultyIdAndCategoryIdAndIdUserOrShared(Integer difficultyId, Integer categoryId, int idUser,
			boolean shared);

	@Query("SELECT r FROM Recipe r WHERE (r.difficulty.id = :difficultyId) AND (r.idUser = :idUser OR r.shared = :shared)")
	List<Recipe> findByDifficultyIdAndIdUserOrShared(Integer difficultyId, int idUser, boolean shared);

	@Query("SELECT r FROM Recipe r WHERE (r.category.id = :categoryId) AND (r.idUser = :idUser OR r.shared = :shared)")
	List<Recipe> findByCategoryIdAndIdUserOrShared(int categoryId, int idUser, boolean shared);

	@Query("SELECT r FROM Recipe r " + "JOIN r.ingredients i " + "WHERE (r.idUser = :idUser OR r.shared = :shared) "
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByIdUserOrShared(int idUser, boolean shared, List<String> ingredientNames, int ingredientCount);

	@Query("SELECT r FROM Recipe r WHERE (r.idUser = :idUser OR r.shared = :shared)")
	List<Recipe> findByIdUserOrShared(int idUser, boolean shared);

	// Consultas Admin
	@Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE (r.difficulty.id = :difficultyId)"
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByDifficultyIdAndIngredients(Integer difficultyId, List<String> ingredientNames,
			int ingredientCount);

	@Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE (r.category.id = :categoryId)"
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByCategoryIdAndIngredients(int categoryId, List<String> ingredientNames, int ingredientCount);

	@Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE (r.difficulty.id = :difficultyId AND r.category.id = :categoryId)"
			+ "AND i.name IN :ingredientNames " + "GROUP BY r " + "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByDifficultyIdAndCategoryIdAndIngredients(Integer difficultyId, Integer categoryId,
			List<String> ingredientNames, int ingredientCount);

	@Query("SELECT r FROM Recipe r WHERE (r.difficulty.id = :difficultyId AND r.category.id = :categoryId)")
	List<Recipe> findByDifficultyIdAndCategoryId(Integer difficultyId, Integer categoryId);

	@Query("SELECT r FROM Recipe r WHERE (r.difficulty.id = :difficultyId)")
	List<Recipe> findByDifficultyId(Integer difficultyId);

	@Query("SELECT r FROM Recipe r WHERE (r.category.id = :categoryId)")
	List<Recipe> findByCategoryId(int categoryId);

	@Query("SELECT r FROM Recipe r " + "JOIN r.ingredients i " + "WHERE i.name IN :ingredientNames " + "GROUP BY r "
			+ "HAVING COUNT(i) >= :ingredientCount")
	List<Recipe> findByIngredients(List<String> ingredientNames, int ingredientCount);

}
