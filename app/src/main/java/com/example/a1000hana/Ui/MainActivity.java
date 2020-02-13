package com.example.a1000hana.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;

import com.example.a1000hana.R;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener {
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        if(savedInstanceState ==null){
            fragmentManager.beginTransaction()
                    .add(R.id.login_fragment_container,loginFragment,getString(R.string.fragment_tag_login))
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
