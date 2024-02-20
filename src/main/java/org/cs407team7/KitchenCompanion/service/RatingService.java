package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.Rating;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.repository.RatingRepository;
import org.cs407team7.KitchenCompanion.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, RecipeRepository recipeRepository, RecipeService recipeService) {
        this.ratingRepository = ratingRepository;
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }

    public Rating addRating(Long recipeId, Long createdBy, Long ratingValue) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        ratingRepository.findByCreatedByAndRecipe(createdBy, recipeId)
                .ifPresent(f -> {
                    throw new IllegalArgumentException("Cannot make multiple ratings on a single recipe");
                });
        Rating rating = new Rating();
        rating.setRecipe(recipe);
        rating.setCreatedBy(createdBy);
        rating.setRating(ratingValue);
        System.out.println(rating);

        rating = ratingRepository.save(rating);
        recipe.addRating(rating);
        recipeRepository.save(recipe);
        return rating;
    }

    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating with id " + id + " not found"));
    }

    // Add more service methods as needed
}
