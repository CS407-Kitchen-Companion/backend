package org.cs407team7.KitchenCompanion.controller;

import org.cs407team7.KitchenCompanion.entity.Comment;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.cs407team7.KitchenCompanion.service.CommentService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService; // Assuming a UserService that handles user authentication

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> payload) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "You must be logged in to post a comment."));
        }

        try {
            Long recipeId = ((Number) payload.get("recipe_id")).longValue();
            String content = (String) payload.get("content");
            Long parentCommentId = payload.containsKey("parent_comment_id") ? ((Number) payload.get("parent_comment_id")).longValue() : null;

            Comment newComment = new Comment();
            newComment.setRecipe(recipeId);
            newComment.setContent(content);
            newComment.setCreatedBy(user.getId());
            newComment.setParentCommentId(parentCommentId);

            Comment createdComment = commentService.addComment(newComment);
            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(createdComment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Invalid request data."));
        }
    }
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<?> getCommentsByRecipe(@PathVariable Long recipeId) {
        try {
            List<Comment> comments = commentService.getCommentsByRecipe(recipeId);
            List<Map<String, Object>> response = new ArrayList<>();

            for (Comment comment : comments) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());

                userService.findUsernameById(comment.getCreatedBy()).ifPresentOrElse(
                        username -> commentData.put("authorUsername", username),
                        () -> commentData.put("authorUsername", "Anonymous")
                );
                commentData.put("parentCommentId", comment.getParentCommentId());

                response.add(commentData);
            }

            return ResponseEntity.ok(new GenericResponse(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "An error occurred."));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getComment(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getComment(id);
        return comment.map(value -> ResponseEntity.ok(new GenericResponse(value)))
                .orElseGet(() -> new ResponseEntity<>(new GenericResponse(404, "Comment not found."), HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Unauthorized"));
        }

        try {
            String content = (String) payload.get("content");
            Optional<Comment> existingComment = commentService.getComment(id);
            if (existingComment.isPresent() && existingComment.get().getCreatedBy().equals(user.getId())) {
                existingComment.get().setContent(content);
                Comment updatedComment = commentService.updateComment(existingComment.get());
                return ResponseEntity.ok(new GenericResponse(updatedComment));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "Forbidden"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Invalid request"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Unauthorized"));
        }

        Optional<Comment> comment = commentService.getComment(id);
        if (comment.isPresent() && comment.get().getCreatedBy().equals(user.getId())) {
            commentService.deleteComment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "Forbidden"));
        }
    }

    @PutMapping("/{id}/addPhoto")
    public ResponseEntity<?> updateCommentPhoto(@PathVariable Long id, @RequestParam String photo) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Unauthorized"));
        }

        Optional<Comment> optionalComment = commentService.getComment(id);
        if (optionalComment.isPresent()
                && optionalComment.get().getCreatedBy().equals(user.getId())) {//a person can only modify his own comment
            Comment comment = optionalComment.get();
            comment.setCommentPhoto(photo);
            commentService.addComment(comment);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "Forbidden"));
        }
    }

    @PutMapping("/{id}/deletePhoto")
    public ResponseEntity<?> deleteCommentPhoto(@PathVariable Long id) {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Unauthorized"));
        }

        Optional<Comment> optionalComment = commentService.getComment(id);
        if (optionalComment.isPresent()
                && optionalComment.get().getCreatedBy().equals(user.getId())) { //a person can only delete his own comment
            Comment comment = optionalComment.get();
            comment.setCommentPhoto(null);
            commentService.addComment(comment);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "Forbidden"));
        }
    }

}
