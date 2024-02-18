package org.cs407team7.KitchenCompanion.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "ingredients")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class IngredientData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    // TODO: Future nutrition info

    @CreatedDate
    private Instant createdAt;

    @Column(name = "is_edited")
    private boolean isEdited = false;

//    private Long editCount;

//    private boolean pinned = false;

    @LastModifiedDate
    private Instant updatedAt;
}
