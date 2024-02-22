package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findById(long id);

    List<Recipe> findAllByIdIn(List<Long> id);

    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t IN :tags GROUP BY r HAVING COUNT(DISTINCT t) = :tagCount")
    List<Recipe> findByAllTags(@Param("tags") List<String> tags, @Param("tagCount") Long tagCount);


}
