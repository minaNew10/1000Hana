package com.example.alfhana.ui.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;


import android.os.Bundle;

import com.example.alfhana.R;
import com.example.alfhana.data.model.User;

import static androidx.navigation.fragment.NavHostFragment.findNavController;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final int RC_START_SIGN_UP = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


}
