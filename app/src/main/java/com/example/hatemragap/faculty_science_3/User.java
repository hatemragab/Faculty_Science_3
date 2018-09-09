package com.example.hatemragap.faculty_science_3;

public class User {
    private String name;
    private String id;
    private String email;
    private String token;

    public User() {
    }

    public User(String name, String id, String email, String token) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
