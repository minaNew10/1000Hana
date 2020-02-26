package com.example.a1000hana.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1000hana.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    TextInputLayout mEtxtLayoutEmail;
    TextInputLayout mEtxtLayoutPsswrd;
    Button mBtnLogin;
    TextView mTxtvRegister;
    LoginViewModel loginViewModel;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Firebase Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if(loginFormState == null)
                    return;
                if(loginFormState.isDataValid()) {
                    mBtnLogin.setEnabled(true);
                    mBtnLogin.setAlpha(1);
                    mBtnLogin.setElevation(4f);
                }

                if (loginFormState.getUsernameError() != null) {
                    mEtxtLayoutEmail.getEditText().setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    Integer pssWrdErr = loginFormState.getPasswordError();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    mEtxtLayoutPsswrd.getEditText().setError(getString(pssWrdErr));

                }

            }
        });
        TextView txtvTitle = findViewById(R.id.txtv_app_name_login);
        TextView btnLogin = findViewById(R.id.btn_login);
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/Adore You.ttf");

        txtvTitle.setTypeface(face);
        btnLogin.setTypeface(face);

        mEtxtLayoutEmail = findViewById(R.id.etxt_layout_email_login);
        mEtxtLayoutPsswrd = findViewById(R.id.etxt_layout_psswrd_login);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        mTxtvRegister = findViewById(R.id.txtv_register);
        mTxtvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(mEtxtLayoutEmail.getEditText().getText().toString().trim(),
                        mEtxtLayoutPsswrd.getEditText().getText().toString().trim());
            }
        };

        mEtxtLayoutEmail.getEditText().addTextChangedListener(afterTextChangedListener);
        mEtxtLayoutPsswrd.getEditText().addTextChangedListener(afterTextChangedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user == null)
            Log.d(TAG, "register now: ");
        else
            Log.d(TAG, "session exist: ");
    }

    void register() {
        mFirebaseAuth.createUserWithEmailAndPassword(mEtxtLayoutEmail.getEditText().getText().toString().trim(),
                mEtxtLayoutPsswrd.getEditText().getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "register successful", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthWeakPasswordException) {

                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            }
        });
    }

    void login() {
        mFirebaseAuth.signInWithEmailAndPassword(mEtxtLayoutEmail.getEditText().getText().toString().trim(),
                mEtxtLayoutPsswrd.getEditText().getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }
}
