package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.springframework.data.jpa.domain.Specification;
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

    @Query("SELECT r FROM Recipe r WHERE r.title LIKE %?1%")
    List<Recipe> findByPartialTitle(String partialTitle);

    List<Recipe> findByTagsIn(List<String> tags);

    List<Recipe> findByAppliancesIn(List<String> appliances);

    @Query(value = "SELECT DISTINCT appliances FROM recipe_appliances", nativeQuery = true)
    List<String> findDistinctAppliances();

    @Query(value = "SELECT DISTINCT serves FROM recipes", nativeQuery = true)
    List<String> findDistinctServes();

    @Query(value = "SELECT DISTINCT CASE WHEN time <= 30 THEN '0-30 minutes' WHEN time > 30 AND time <= 60 THEN '31-60 minutes' WHEN time > 60 AND time <= 90 THEN '61-90 minutes' WHEN time > 90 AND time <= 120 THEN '91-120 minutes' ELSE 'Over 120 minutes' END AS time_range FROM recipes", nativeQuery = true)
    List<String> findTimeRanges();

    List<Recipe> findByCaloriesLessThanEqual(Long calories);

    List<Recipe> findAll(Specification<Recipe> spec);



}
