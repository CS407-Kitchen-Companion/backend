package org.cs407team7.KitchenCompanion.controller;

import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }
    @PostMapping(path = "/new")
    public ResponseEntity<Object> addRecipe(
            @RequestBody Map<String, String> payload
    ){
        try {
            String title = payload.get("title");
            String content = payload.get("content");
            Long createdBy = Long.parseLong(payload.get("createdBy"));
            Recipe recipe = new Recipe();
            recipe.setTitle(title);
            recipe.setContent(content);
            recipe.setCreatedBy(createdBy);

            Recipe savedRecipe = recipeService.addRecipe(recipe);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "Internal Server Error"));
        }
    }
}
