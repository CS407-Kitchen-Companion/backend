package org.cs407team7.KitchenCompanion.responseobject;

import org.springframework.security.core.parameters.P;

import java.time.Instant;

public class PublicUserDataResponse {
    public long id;

    public String username;

    public String email;

    public Instant createdAt;

    public PublicUserDataResponse(long id, String username, String email, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }
}
