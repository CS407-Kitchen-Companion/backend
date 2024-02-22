package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        return optionalRecipe.orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe updateRecipe(Long id, Recipe recipe) {
        return null;
    }

    @Override
    public void deleteRecipe(Long id) {
    }

    @Override
    public List<Recipe> getRecipesByAllTags(List<String> tags) {
        Long tagCount = Long.valueOf(tags.size());
        return recipeRepository.findByAllTags(tags, tagCount);
    }

    @Override
    public List<Recipe> getRecipesByPartialTitle(String partialTitle) {
        return recipeRepository.findByPartialTitle(partialTitle);
    }

    @Override
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
