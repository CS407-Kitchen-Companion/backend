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

    @Autowired
    public RatingService(RatingRepository ratingRepository, RecipeRepository recipeRepository) {
        this.ratingRepository = ratingRepository;
        this.recipeRepository = recipeRepository;
    }

    public Rating addRating(Long recipeId, Long createdBy, Long ratingValue) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe with id " + recipeId + " not found"));
        Rating rating = new Rating();
        rating.setRecipe(recipe);
        rating.setCreatedBy(createdBy);
        rating.setRating(ratingValue);
        System.out.println(rating);
        return ratingRepository.save(rating);
    }

    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating with id " + id + " not found"));
    }

    // Add more service methods as needed
}
