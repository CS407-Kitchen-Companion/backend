package org.cs407team7.KitchenCompanion.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "ingredients")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class IngredientAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    private String ingredient;

    private Double amount;

    private String unit;

    private Boolean isVolume;

    private Double standardUnit;

    private Integer parsedUnit;

    public IngredientAmount() {

    }

    public IngredientAmount(Recipe recipe, String ingredient, Double amount, String unit) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
        parseUnit(amount, unit);
    }

    private void parseUnit(Double amount, String unit) {
        switch (unit) {
            case "g" -> {
                standardUnit = amount;
                parsedUnit = 1;
            }
            case "ml" -> {
                standardUnit = amount;
                parsedUnit = 2;
            }
            // Failure case
            default -> parsedUnit = 0;
        }
    }

    public Long getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // TODO: Future nutrition info
}
