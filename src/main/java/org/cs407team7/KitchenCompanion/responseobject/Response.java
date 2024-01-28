package org.cs407team7.KitchenCompanion.responseobject;

abstract class Response {
    String response;
    int status;

    public String getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }
}
