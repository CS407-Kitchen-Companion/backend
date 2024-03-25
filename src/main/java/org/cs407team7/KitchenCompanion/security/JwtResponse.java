package org.cs407team7.KitchenCompanion.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JwtResponse implements Serializable {

    Long response;

    int status;

    boolean error;

    Map<String, String> data;

    public JwtResponse(String token, Long id) {
        data = new HashMap<>();
        data.put("token", token);
        this.response = id;
        this.status = 200;
        this.error = false;
    }

//    public String getToken() {
//        return this.data.get("token");
//    }

    public Long getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

    public boolean isError() {
        return error;
    }

    public Map<String, String> getData() {
        return data;
    }
}