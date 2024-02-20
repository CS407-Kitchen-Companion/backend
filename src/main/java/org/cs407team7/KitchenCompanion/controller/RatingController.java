package org.cs407team7.KitchenCompanion.controller;

import org.cs407team7.KitchenCompanion.entity.Rating;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.service.RatingService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;

import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    public UserService userService;
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody Map<String, Object> payload) {
        User user = userService.getAuthUser();
        Long recipeId = ((Integer) payload.get("recipe_id")).longValue();
        Long rating = ((Integer) payload.get("rating")).longValue();
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse(401, "You must be logged in to create a new rating.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Rating());
        }

        Rating createdRating = ratingService.addRating(recipeId,user.getId(),rating);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        Rating rating = ratingService.getRatingById(id);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    // Add more endpoints as needed
}
