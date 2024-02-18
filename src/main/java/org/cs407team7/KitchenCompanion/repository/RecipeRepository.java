package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findById(long id);
}
