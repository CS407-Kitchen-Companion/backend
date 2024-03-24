package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Find all comments by recipe ID
    List<Comment> findByRecipe(Long recipeId);

    // Find all replies to a comment
    List<Comment> findByParentCommentId(Long parentCommentId);
}
