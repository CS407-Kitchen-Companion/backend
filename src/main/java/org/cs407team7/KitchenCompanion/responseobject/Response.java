package org.cs407team7.KitchenCompanion.responseobject;

abstract class Response {
    String response;
    int status;

    boolean error;

    Object data;

    public String getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

    public boolean isError() {
        return error;
    }

    public Object getData() {
        return data;
    }
}
