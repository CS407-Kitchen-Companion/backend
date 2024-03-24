package org.cs407team7.KitchenCompanion.controller;

import jakarta.validation.Valid;
import org.cs407team7.KitchenCompanion.entity.IngredientAmount;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.IngredientAmountRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/recipe")
@CrossOrigin(origins = "http://localhost:3000/")
public class RecipeController {
    private final RecipeService recipeService;

    public final UserService userService;

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientAmountRepository ingredientAmountRepository;

    @Autowired
    public RecipeController(UserService userService, UserRepository userRepository,
                            RecipeRepository recipeRepository,
                            RecipeService recipeService,
                            IngredientAmountRepository ingredientAmountRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
        this.ingredientAmountRepository = ingredientAmountRepository;
    }

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
            List<IngredientAmount> ingredients = new ArrayList<>();
            Recipe recipe = new Recipe(payload.title, payload.content, createdBy, ingredients,
                    payload.time, payload.serves, payload.calories, payload.tags, payload.appliances);

            recipeService.addRecipe(recipe);

            for (Map<String, String> ingredient : payload.ingredients) {
                IngredientAmount newIngredient = new IngredientAmount(
                        recipe,
                        ingredient.get("ingredient"),
                        Double.parseDouble(ingredient.get("amount")),
                        ingredient.get("unit"));
                ingredientAmountRepository.save(newIngredient);
                ingredients.add(newIngredient);
            }
            // Sure that works, ill change a few things to match this in the future
            recipe = recipeService.addRecipe(recipe);

            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(recipe));
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
            return ResponseEntity.ok(new GenericResponse(201, recipe));
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(404).body(new ErrorResponse(404, "Could not find a recipe with that Id"));
            } else {
                throw e;
            }
        } catch (Exception e) {
            System.out.println(e);
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
    public ResponseEntity<GenericResponse> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) List<String> appliances,
            @RequestParam(required = false) Long calories) {

        if (title == null && tags == null && appliances == null && calories == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse(400, "You must specify at least one filtering option"));
        }

        List<Recipe> recipes = recipeService.searchRecipesByFilters(title, tags, appliances, calories);

        List<Map<String, Object>> responseRecipes = recipes.stream()
                .map(recipe -> {
                    Map<String, Object> responseRecipe = new HashMap<>();
                    responseRecipe.put("title", recipe.getTitle());
                    responseRecipe.put("rating", recipe.getCalculatedRating() / recipe.getRatingCount());
                    responseRecipe.put("serving size", recipe.getServes());
                    responseRecipe.put("calories", recipe.getCalories());
                    responseRecipe.put("time", recipe.getTime());
                    responseRecipe.put("tags", recipe.getTags());

                    String ingredientsString = recipe.getIngredients().stream()
                            .map(entry -> entry.getName() + " " + entry.getAmount() + entry.getUnit())
                            .collect(Collectors.joining(" · "));

                    if (ingredientsString.length() > 100) {
                        int lastWhitespaceIndex = ingredientsString.substring(0, 100).lastIndexOf(' ');
                        if (lastWhitespaceIndex != -1) {
                            ingredientsString = ingredientsString.substring(0, lastWhitespaceIndex) + " ...";
                        } else {
                            ingredientsString = ingredientsString.substring(0, 100) + " ...";
                        }
                    }

                    responseRecipe.put("ingredients", ingredientsString);
                    return responseRecipe;
                })
                .collect(Collectors.toList());


        if (!recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(responseRecipes));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GenericResponse(404, "No recipes found with the specified criteria"));
    }

    @GetMapping(path = "/search/titles")
    public ResponseEntity<GenericResponse> searchRecipesByPartialTitle(
            @RequestParam String title,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) List<String> appliances,
            @RequestParam(required = false) Long calories) {

        if (title.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse(400, "Partial title cannot be empty"));
        }

        List<Recipe> recipes = recipeService.searchRecipesByFilters(title, tags, appliances, calories);
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericResponse(404, "No recipes found"));
        }

        List<String> titles = recipes.stream()
                .map(Recipe::getTitle)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(titles));
    }

    @GetMapping(path = "/search/filters")
    public ResponseEntity<GenericResponse> getFilters() {
        String[] filters = {"title", "calories", "appliances", "tags"};
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(filters));
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
