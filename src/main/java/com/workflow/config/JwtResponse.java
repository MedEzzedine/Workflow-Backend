package com.workflow.config;


import com.workflow.entity.User;

public class JwtResponse {

    private String token;
    private User user;

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String token, User user) {
        super();
        this.token = token;
        this.user = user;
    }

    public User getuser() {
        return user;
    }

    public void setClient(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}