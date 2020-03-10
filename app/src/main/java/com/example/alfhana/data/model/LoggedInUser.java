package com.example.alfhana.data.model;

public class LoggedInUser {

    private String displayName;
    private String email;
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
