package com.vigneshtraining.assignment53.model;

import android.graphics.Bitmap;

/**
 * Created by vimadhavan on 3/19/2017.
 */

public class SelectUser {

    private Bitmap thumb;
    private String name,phone,email;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
