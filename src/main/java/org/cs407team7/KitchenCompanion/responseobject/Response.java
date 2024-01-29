package org.cs407team7.KitchenCompanion.responseobject;

abstract class Response {
    String response;
    int status;

    boolean error;

    public String getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

    public boolean isError() {
        return error;
    }
}
