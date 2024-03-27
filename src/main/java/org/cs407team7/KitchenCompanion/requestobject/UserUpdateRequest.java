package org.cs407team7.KitchenCompanion.requestobject;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(max = 256)
    private String photo;
    @Size(max = 1024)
    private String details;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}