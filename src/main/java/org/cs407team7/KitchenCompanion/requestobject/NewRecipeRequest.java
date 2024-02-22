package org.cs407team7.KitchenCompanion.requestobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class NewRecipeRequest {

    @NotBlank
    public String title;
    @NotNull
    public List<String> content;
    @NotNull
    public Long time;
    @NotNull
    public Long serves;

    // Optional
    public Long calories;
    @NotNull
    public List<String> tags;
    @NotNull
    public Map<String, String> ingredients;
    @NotNull
    public List<String> appliances;
}
