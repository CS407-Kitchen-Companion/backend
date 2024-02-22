package org.cs407team7.KitchenCompanion.controller;

import jakarta.validation.Valid;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.RecipeRepository;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.cs407team7.KitchenCompanion.requestobject.NewRecipeRequest;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.cs407team7.KitchenCompanion.service.RecipeService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/recipe")
@CrossOrigin(origins = "http://localhost:3000/")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping(path = "/new")
    public ResponseEntity<Object> addRecipe(
            @RequestBody @Valid NewRecipeRequest payload
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
            if (payload.calories == null) {
                payload.calories = 99L;
            }

            Recipe recipe = new Recipe(payload.title, payload.content, createdBy, payload.ingredients,
                    payload.time, payload.serves, payload.calories, payload.tags, payload.appliances);

            // Sure that works, ill change a few things to match this in the future
            Recipe savedRecipe = recipeService.addRecipe(recipe);

            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(savedRecipe));
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
                return ResponseEntity.ok(new GenericResponse(201, recipe));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Internal Server Error"));
        }
    }

    @GetMapping(path = "/get")
    public ResponseEntity<Object> getRecipeMulti(
            @RequestBody Map<String, List<Long>> payload
    ) {
        try {
            List<Recipe> recipes = recipeRepository.findAllByIdIn(payload.get("recipes"));
            return ResponseEntity.ok(new GenericResponse(201, recipes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Internal Server Error"));
        }
    }

    @GetMapping(path = "/{id}/save")
    public ResponseEntity<Object> saveRecipe(@PathVariable Long id) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to see saved recipes."));
        }
        try {
            recipeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Could not find a recipe with that name"));
            user = userRepository.findById(user.getId()).orElse(null);
            user.getSavedRecipes().add(id);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(user.getSavedRecipes()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(404, "Could not find a recipe with that name"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error"));

        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<GenericResponse> getRecipesByTags(@RequestParam List<String> tags) {
        if (tags.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(400, "You must specify at least one tag"));
        }

        List<Recipe> recipes = recipeService.getRecipesByAllTags(tags);
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(404, "No recipes found with the specified tags"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(recipes));
    }

    @GetMapping(path = "/search/titles")
    public ResponseEntity<GenericResponse> searchRecipesByPartialTitle(@RequestParam String partialTitle) {
        if (partialTitle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse(400, "Partial title cannot be empty"));
        }

        List<Recipe> recipes = recipeService.getRecipesByPartialTitle(partialTitle);
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericResponse(404, "No recipes found with the partial title: " + partialTitle));
        }

        List<String> titles = recipes.stream()
                .map(Recipe::getTitle)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(titles));
    }


    @GetMapping(path = "/{id}/rating")
    public ResponseEntity<Object> getRecipeRating(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            if (recipe != null) {
                Long calculatedRating = recipe.getCalculatedRating();
                return ResponseEntity.ok(new GenericResponse(200, calculatedRating));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Internal Server Error"));
        }
    }




}
