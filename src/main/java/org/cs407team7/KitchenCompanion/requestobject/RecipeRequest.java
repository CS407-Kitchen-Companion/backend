package org.cs407team7.KitchenCompanion.requestobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class RecipeRequest {

    @NotBlank
    public String title;
    @NotBlank
    public String content;
    @NotNull
    public Long time;
    @NotNull
    public Long serves;
    @NotNull
    public List<String> tags;
    @NotNull
    public Map<String, String> ingredients;
    @NotNull
    public List<String> appliances;
}
