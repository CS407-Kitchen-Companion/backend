package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.IngredientAmount;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class NutritionService {


    // Maybe don't hardcode it here
    private static final String apikey = "ap4bd1J8cCkGi0pRSdVUh7jlGCsDnJ2HDhqcvInV";
    private static final String urlbase = "https://api.nal.usda.gov/fdc/v1/";

    public Long estimateCalories(List<IngredientAmount> ingredients) {
        return (long) ingredients.size();
    }

    public void searchOne(String ingredientName) {

    }
}
