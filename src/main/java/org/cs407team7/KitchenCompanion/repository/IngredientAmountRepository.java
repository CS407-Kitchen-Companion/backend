package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.IngredientAmount;
import org.cs407team7.KitchenCompanion.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientAmountRepository extends JpaRepository<IngredientAmount, Long> {
    Optional<IngredientAmount> findById(long id);

    Optional<IngredientAmount> findByRecipeId(long recipe);
}