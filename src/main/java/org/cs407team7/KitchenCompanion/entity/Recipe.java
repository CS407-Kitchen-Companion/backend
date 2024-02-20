package org.cs407team7.KitchenCompanion.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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

    private String content;

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

    @ElementCollection
    private Map<String, String> ingredients;
//    private List<Long> ingredients;

//    private Map<IngredientData, String> ingredients;

    private Long ratingCount;

    private Long calculatedRating;

    @LastModifiedDate
    private Instant updatedAt;

    public Recipe() {
        comments = new ArrayList<>();
        ratings = new ArrayList<>();
        ingredients = new TreeMap<>();
        ratingCount = 0L;
        calculatedRating = 0L;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
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

    public void setContent(String content) {
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
        return calculatedRating;
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

    public Map<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, String> ingredients) {
        this.ingredients = ingredients;
    }

}
