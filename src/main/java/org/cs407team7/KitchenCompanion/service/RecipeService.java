package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe addRecipe(Recipe recipe);
    Recipe getRecipeById(Long id);
    List<Recipe> getAllRecipes();
    Recipe updateRecipe(Long id, Recipe recipe);
    void deleteRecipe(Long id);
     List<Recipe> getRecipesByAllTags(List<String> tags);
}
