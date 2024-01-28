package org.cs407team7.KitchenCompanion.responseobject;

public class ErrorResponse extends Response{

    public ErrorResponse(int code, String error) {
        this.response = error;
        this.status = code;
    }

    public ErrorResponse(String error) {
        this.response = error;
        this.status = 500;
    }
}
