package com.example.android.chatapp.POJO;

import java.io.Serializable;

/**
 * Created by eslam on 08-Jan-18.
 */

public class User implements Serializable {
    private String photoUrl;
    private String name;
    private String email;

    public User() {
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
