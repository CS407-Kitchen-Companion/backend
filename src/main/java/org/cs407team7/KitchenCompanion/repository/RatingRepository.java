package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.Rating;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findById(long id);

    Optional<Rating> findByCreatedByAndRecipe(long createdBy, long recipe);
}
