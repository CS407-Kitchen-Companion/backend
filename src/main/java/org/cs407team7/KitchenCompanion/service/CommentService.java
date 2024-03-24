package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.Comment;
import org.cs407team7.KitchenCompanion.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // Get all comments for a recipe
    public List<Comment> getCommentsByRecipe(Long recipeId) {
        return commentRepository.findByRecipe(recipeId);
    }

    // Get all replies to a comment
    public List<Comment> getReplies(Long parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }

    // Get a single comment by ID
    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment updateComment(Comment updatedComment) {
        return commentRepository.save(updatedComment);
    }
}
