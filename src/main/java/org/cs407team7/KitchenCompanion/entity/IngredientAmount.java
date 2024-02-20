package org.cs407team7.KitchenCompanion.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Deprecated
/**
 * Dont use, havent decided if usage is ideal.
 */
public class IngredientAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    // TODO: Future nutrition info

    @CreatedDate
    private Instant createdAt;

//    private Long editCount;

//    private boolean pinned = false;

    @LastModifiedDate
    private Instant updatedAt;
}
