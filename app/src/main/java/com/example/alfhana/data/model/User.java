package com.example.alfhana.data.model;

import android.os.Parcelable;

import org.parceler.Parcel;

@Parcel
public class User {

    public String displayName;
    public String email;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String phone;
    public String address;
    public String image;



    //required to prevent error
    public User() {
    }

    public User(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public User(String displayName, String email, String phone, String address) {
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        image = null;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
