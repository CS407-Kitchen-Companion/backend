package org.cs407team7.KitchenCompanion.controller;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "comments")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @JsonManagedReference
//    @ManyToOne
//    @JoinColumn(name = "post_id", nullable = false)
    private Long recipie;

    //    @JsonManagedReference
//    @ManyToMany
//    @JoinColumn(name = "comment_id", nullable = false)

// TODO: Future

//    @ElementCollection
//    private List<Long> comments;
//    private boolean isReply;

    private String content;

    //    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "created_by_id", nullable = false)
    @CreatedBy
    private Long createdBy;

    public Comment() {
        comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
