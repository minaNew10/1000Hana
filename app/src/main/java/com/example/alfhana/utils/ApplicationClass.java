package com.example.alfhana.utils;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class ApplicationClass extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this); // initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // enable offline capability
    }
}
