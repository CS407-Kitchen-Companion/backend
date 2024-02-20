package org.cs407team7.KitchenCompanion.controller;

import org.cs407team7.KitchenCompanion.entity.Rating;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.cs407team7.KitchenCompanion.service.RatingService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;

import java.util.Map;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    public UserService userService;
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Object> addRating(@RequestBody Map<String, Object> payload) {
        User user = userService.getAuthUser();
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ErrorResponse(401, "You must be logged in to create a new rating."));
            }

            Long recipeId = ((Integer) payload.get("recipe_id")).longValue();
            Long rating = ((Integer) payload.get("rating")).longValue();

            Rating createdRating = ratingService.addRating(recipeId, user.getId(), rating);
            return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(400, e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        Rating rating = ratingService.getRatingById(id);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    // Add more endpoints as needed
}
