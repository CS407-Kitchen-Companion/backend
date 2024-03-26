package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.IngredientAmount;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.IngredientAmountRepository;
import org.cs407team7.KitchenCompanion.repository.RecipeRepository;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.cs407team7.KitchenCompanion.requestobject.NewRecipeRequest;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientAmountRepository ingredientAmountRepository;

    public final UserService userService;

    private final UserRepository userRepository;

    private final NutritionService nutritionService;


    @Autowired
    public RecipeService(UserService userService, UserRepository userRepository,
                         RecipeRepository recipeRepository, NutritionService nutritionService,
                         IngredientAmountRepository ingredientAmountRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.nutritionService = nutritionService;
        this.ingredientAmountRepository = ingredientAmountRepository;
    }

    public ResponseEntity<?> createRecipie(NewRecipeRequest payload, Long createdBy) {
        // TODO: check non null values

        List<IngredientAmount> ingredients = new ArrayList<>();
        Recipe recipe = new Recipe(payload.title, payload.content, createdBy, ingredients,
                payload.time, payload.serves, payload.calories, payload.tags, payload.appliances);

        addRecipe(recipe);

        parseCreateIngredient(payload, ingredients, recipe);

        if (recipe.getCalories() == null) {
            recipe.setCalories(nutritionService.estimateCalories(ingredients));
        }

        recipe = addRecipe(recipe);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(recipe));
    }

    public ResponseEntity<?> editRecipe(NewRecipeRequest payload, User user) {
        Recipe recipe = getRecipeById(payload.id);
        if (!recipe.getCreatedBy().equals(user.getId())) {
            throw new
                    ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "You may only edit your own recipes");
        }
        recipe.largeEdit(payload.title, payload.content, payload.time,
                payload.serves, payload.calories, payload.tags, payload.appliances);

        recipe.setE

        addRecipe(recipe);

        List<IngredientAmount> ingredients = new ArrayList<>(recipe.getIngredients());
        recipe.getIngredients().clear();
        addRecipe(recipe);

        ingredientAmountRepository.deleteAll(ingredients);
        ingredients = new ArrayList<>();

        parseCreateIngredient(payload, ingredients, recipe);
        recipe.setCalories(nutritionService.estimateCalories(ingredients));
        recipe.setIngredients(ingredients);

        recipe = addRecipe(recipe);

        return ResponseEntity.ok(new GenericResponse(recipe));
    }

    private void parseCreateIngredient(NewRecipeRequest payload, List<IngredientAmount> ingredients, Recipe recipe) {
        for (Map<String, String> ingredient : payload.ingredients) {
            IngredientAmount newIngredient = new IngredientAmount(
                    recipe,
                    ingredient.get("ingredient"),
                    Double.parseDouble(ingredient.get("amount")),
                    ingredient.get("unit"));
            ingredientAmountRepository.save(newIngredient);
            ingredients.add(newIngredient);
        }
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
