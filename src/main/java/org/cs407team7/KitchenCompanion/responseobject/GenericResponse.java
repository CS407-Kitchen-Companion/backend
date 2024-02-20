package org.cs407team7.KitchenCompanion.responseobject;

public class GenericResponse extends Response {

    public GenericResponse(String response) {
        this();
        this.response = response;
    }

    public GenericResponse(int status, String response) {
        this();
        this.response = response;
        this.status = status;
    }

    public GenericResponse(String response, Object data) {
        this();
        this.response = response;
        this.data = data;
    }

    public GenericResponse(int status, String response, Object data) {
        this();
        this.response = response;
        this.status = status;
        this.data = data;
    }

    public GenericResponse(Object data) {
        this();
        this.response = "See Data.";
        this.data = data;
    }

    public GenericResponse(int status, Object data) {
        this();
        this.status = status;
        this.response = "See Data.";
        this.data = data;
    }

    public GenericResponse() {
        this.status = 200; // Default Value
        this.error = false;
    }
}
