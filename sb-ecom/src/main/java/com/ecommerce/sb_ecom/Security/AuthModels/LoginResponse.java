package com.ecommerce.sb_ecom.Security.AuthModels;

import java.util.List;

public class LoginResponse {

    private String jwtToken;
    private String username;
    private List<String> roles;

    // constucteur pour bearer token
    public LoginResponse(String username, List<String> roles, String jwtToken) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }

    // constructeur pour jwt cookie
    public LoginResponse(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
