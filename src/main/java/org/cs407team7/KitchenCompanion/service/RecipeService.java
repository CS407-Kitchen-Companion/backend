package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipeById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        return optionalRecipe.orElseThrow(() -> new
                ResponseStatusException(
                HttpStatus.NOT_FOUND, "Could not find a recipie with that id")
        );
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe updateRecipe(Long id, Recipe recipe) {
        return null;
    }

    public void deleteRecipe(Long id) {
    }

    public List<Recipe> getRecipesByAllTags(List<String> tags) {
        Long tagCount = Long.valueOf(tags.size());
        return recipeRepository.findByAllTags(tags, tagCount);
    }

    public List<Recipe> getRecipesByPartialTitle(String partialTitle) {
        return recipeRepository.findByPartialTitle(partialTitle);
    }

    public List<Recipe> searchRecipesByFilters(String title, List<String> tags, List<String> appliances, Long calories) {
        Specification<Recipe> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (tags != null && !tags.isEmpty()) {
            spec = spec.and((root, query, builder) -> root.join("tags").in(tags));
        }

        if (appliances != null && !appliances.isEmpty()) {
            spec = spec.and((root, query, builder) -> root.join("appliances").in(appliances));
        }

        if (calories != null) {
            spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("calories"), calories));
        }

        return recipeRepository.findAll(spec);
    }
}
