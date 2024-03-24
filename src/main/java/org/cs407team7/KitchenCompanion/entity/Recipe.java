package org.cs407team7.KitchenCompanion.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nimbusds.jose.crypto.impl.AAD;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "recipes")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @ElementCollection
    private List<String> content;

    //    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "created_by_id", nullable = false)
//    @CreatedBy
    // User, trying int instead of object/joining by default
    private Long createdBy;

    @CreatedDate
    private Instant createdAt;

    @Column(name = "is_edited")
    private boolean isEdited = false;

//    private Long editCount;

    //    private boolean pinned = false;
    @ElementCollection
    private List<Long> comments;


    //    @OneToMany(mappedBy = "recipe")
    @ElementCollection
    private List<Long> ratings;

    // TODO: make entity with limited list not user supplied randomness
    @ElementCollection
    private List<String> appliances;

//    @ElementCollection
//    private List<String> steps;

    // TODO: make entity with limited list not user supplied randomness
    @ElementCollection
    private List<String> tags;

    @JsonManagedReference
    @OneToMany
    @NotNull
    @JoinTable(
            name = "ingredient_list",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "recipie_id")
    )
    private List<IngredientAmount> ingredients;
//    private List<Long> ingredients;

//    private Map<IngredientData, String> ingredients;

    private Long ratingCount;

    private Long calculatedRating;

    private Long serves;

    private Long time;

    private Long calories;

    @LastModifiedDate
    private Instant updatedAt;

    public Recipe() {
        comments = new ArrayList<>();
        ratings = new ArrayList<>();
        ingredients = new ArrayList<>();
        appliances = new ArrayList<>();
//        steps = new ArrayList<>();
        tags = new ArrayList<>();
        ratingCount = 0L;
        calculatedRating = 0L;
        calories = 0L;
    }

    public Recipe(String title, List<String> content, Long createdBy, List<IngredientAmount> ingredients,
                  Long time, Long serves, Long calories, List<String> tags, List<String> appliances) {
        this();
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.ingredients = ingredients;
        this.time = time;
        this.serves = serves;
        this.calories = calories;
        this.tags = tags;
        this.appliances = appliances;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getContent() {
        return content;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public Long getCalculatedRating() {

        if (ratingCount == 0) {
            return 0L;
        }
        return (long) (calculatedRating / (double) ratingCount);
    }

    public List<Long> getRatings() {
        return ratings;
    }

    public void setRatings(List<Long> ratings) {
        this.ratings = ratings;
    }

    public void addRating(Rating rating) {
        if (!rating.getRecipe().equals(getId())) {
            return; //invalid
        }
        ratingCount++;
        ratings.add(rating.getId());
        calculatedRating += rating.getRating();
    }

    public void removeRating(Rating rating) {

    }

    public List<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientAmount> ingredients) {
        this.ingredients = ingredients;
    }

    public Long getServes() {
        return serves;
    }

    public void setServes(Long serves) {
        this.serves = serves;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<String> getAppliances() {
        return appliances;
    }

    public void setAppliances(List<String> appliances) {
        this.appliances = appliances;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getCalories() {
        return calories;
    }

    public void setCalories(Long calories) {
        this.calories = calories;
    }
}
