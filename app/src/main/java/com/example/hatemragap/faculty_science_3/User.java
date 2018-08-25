package com.example.hatemragap.faculty_science_3;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Serializable {
    private final String name;
    private final String id;
    private final String imgUrl;
    private final String email;

    public User(FirebaseUser firebaseUser) {
        this.name = firebaseUser.getDisplayName();
        this.id = firebaseUser.getUid();
        this.imgUrl = firebaseUser.getPhotoUrl().toString();
        this.email = firebaseUser.getEmail();
    }

    public User(String name, String id, String imgUrl, String email) {
        this.name = name;
        this.id = id;
        this.imgUrl = imgUrl;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getEmail() {
        return email;
    }
}
