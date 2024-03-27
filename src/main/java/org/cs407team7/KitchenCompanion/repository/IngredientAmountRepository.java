package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.IngredientAmount;
import org.cs407team7.KitchenCompanion.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientAmountRepository extends JpaRepository<IngredientAmount, Long> {
    Optional<IngredientAmount> findById(long id);

    Optional<IngredientAmount> findByRecipeId(long recipe);

    @Modifying
    @Query(value = "DELETE FROM ingredients WHERE recipe_id = :recipeId", nativeQuery = true)
    void deleteByRecipeId(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(value = "DELETE FROM ingredient_list WHERE ingredient_id = :recipeId", nativeQuery = true)
    void deleteByRecipeIdSec(@Param("recipeId") Long recipeId);

}