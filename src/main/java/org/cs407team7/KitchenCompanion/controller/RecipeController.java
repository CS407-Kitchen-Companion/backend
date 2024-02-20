package org.cs407team7.KitchenCompanion.controller;

import jakarta.validation.Valid;
import net.minidev.json.JSONObject;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.requestobject.RecipeRequest;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.service.RecipeService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public UserService userService;

    @PostMapping(path = "/new")
    public ResponseEntity<Object> addRecipe(
            @RequestBody @Valid RecipeRequest payload
    ) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to create a new recipe."));
        }
        try {
            // Use the getAuthUser() in the future.
            Long createdBy = user.getId();

            // TODO: check non null values

            Recipe recipe = new Recipe(payload.title, payload.content, createdBy, payload.ingredients,
                    payload.time, payload.serves, payload.tags, payload.appliances);

            // Sure that works, ill change a few things to match this in the future
            Recipe savedRecipe = recipeService.addRecipe(recipe);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error"));

        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getRecipe(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            if (recipe != null) {
                return ResponseEntity.ok(recipe);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Internal Server Error"));
        }
    }


}
