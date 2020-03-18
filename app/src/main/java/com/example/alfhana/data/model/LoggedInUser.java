package com.example.alfhana.data.model;

import android.os.Parcelable;

import org.parceler.Parcel;

@Parcel
public class LoggedInUser{

    public String displayName;
    public String email;
    public String phone;
    public String address;

    //required to prevent error
    public LoggedInUser() {
    }

    public LoggedInUser(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

}
