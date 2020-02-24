package com.example.a1000hana.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1000hana.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    EditText mEtxtEmail;
    EditText mEtxtvPsswrd;
    Button mBtnLogin;
    TextView mTxtvRegister;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        TextView txtvTitle = findViewById(R.id.txtv_app_name_login);
        TextView btnLogin = findViewById(R.id.btn_login);
        Typeface face = Typeface.createFromAsset(this.getAssets(),"fonts/Adore You.ttf");
        txtvTitle.setTypeface(face);
        btnLogin.setTypeface(face);
        mEtxtEmail = findViewById(R.id.etxt_email_login);
        mEtxtvPsswrd = findViewById(R.id.etxt_psswrd_login);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        if (user==null)
            Log.d(TAG, "register now: ");
        else
            Log.d(TAG, "session exist: ");
    }
    void register(){
        mFirebaseAuth.createUserWithEmailAndPassword(mEtxtEmail.getText().toString(),mEtxtvPsswrd.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"register successful",Toast.LENGTH_LONG).show();
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
                if(e instanceof FirebaseAuthWeakPasswordException){

                    Log.d(TAG, "onFailure: "+e.getMessage());
                }
            }
        });
    }

    void login(){
        mFirebaseAuth.signInWithEmailAndPassword(mEtxtEmail.getText().toString(),mEtxtvPsswrd.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"login successful",Toast.LENGTH_LONG).show();
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
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
}
