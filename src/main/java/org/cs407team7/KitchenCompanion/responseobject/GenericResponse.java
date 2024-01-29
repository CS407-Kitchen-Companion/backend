package org.cs407team7.KitchenCompanion.responseobject;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GenericResponse extends Response {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object data;

    public GenericResponse(String response) {
        this.response = response;
        this.status = 200; // Default Value
        this.error = false;
    }

    public GenericResponse(int status, String response) {
        this.response = response;
        this.status = status;
        this.error = false;
    }

    public GenericResponse(int status, String response, Object data) {
        this.response = response;
        this.status = status;
        this.data = data;
        this.error = false;
    }

}
