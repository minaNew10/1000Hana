package com.example.alfhana.ui.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.alfhana.R;
import com.example.alfhana.data.model.LocationHelper;
import com.example.alfhana.data.model.User;
import com.example.alfhana.ui.loginactivity.loginfragment.LoginFragment;
import com.example.alfhana.ui.loginactivity.signup.SignUpFragment;

import static androidx.navigation.fragment.NavHostFragment.findNavController;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    SignUpFragment signUpFragment;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
