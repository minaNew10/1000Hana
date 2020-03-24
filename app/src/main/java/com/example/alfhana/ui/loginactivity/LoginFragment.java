package com.example.alfhana.ui.loginactivity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.alfhana.R;
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int RC_START_SIGN_UP = 1;
    User LoggedInUser;
    LoginViewModel loginViewModel;
    FragmentLoginBinding loginBinding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    User loggedInUser;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        View v = loginBinding.getRoot();
        loginBinding.setSubmit(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().getRoot();
        loginViewModel = ViewModelProviders.of(this.getActivity()).get(LoginViewModel.class);
        loginViewModel.getLoginFormState().observe(this.getActivity(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if (loginFormState == null)
                    return;
                if (loginFormState.isDataValid()) {
                    loginBinding.btnLogin.setEnabled(true);
                    loginBinding.btnLogin.setAlpha(1);
                    loginBinding.btnLogin.setElevation(4f);
                }

                if (loginFormState.getUsernameError() != null) {
                    loginBinding.etxtEmailLogin.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    Integer pssWrdErr = loginFormState.getPasswordError();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    loginBinding.etxtPsswrdLogin.setError(getString(pssWrdErr));

                }

            }
        });

        Typeface face = Typeface.createFromAsset(this.getActivity().getAssets(), "fonts/Adore You.ttf");

        loginBinding.txtvAppNameLogin.setTypeface(face);
        loginBinding.btnLogin.setTypeface(face);

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
                loginViewModel.loginDataChanged(loginBinding.etxtEmailLogin.getText().toString().trim(),
                        loginBinding.etxtPsswrdLogin.getText().toString().trim());
            }
        };
        loginBinding.etxtEmailLogin.addTextChangedListener(afterTextChangedListener);
        loginBinding.etxtPsswrdLogin.addTextChangedListener(afterTextChangedListener);


        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user == null)
            Log.d(TAG, "register now: ");
        else
            Log.d(TAG, "session exist: ");
        if(getArguments()!= null){
            LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
            if(args.getUser()!= null) {
                loggedInUser = args.getUser();
                loginBinding.etxtEmailLogin.setText(loggedInUser.getEmail());

            }
        }
    }



    public void goToSignUp() {
        NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_signUpFragment);
    }
    public void login() {

        mFirebaseAuth.signInWithEmailAndPassword(loginBinding.etxtEmailLogin.getEditableText().toString().trim(),
                loginBinding.etxtPsswrdLogin.getEditableText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String id = mFirebaseAuth.getUid();
                            retrieveUser(id);



                            isAdmin(id);
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

    private void retrieveUser(final String id) {

        mDatabaseReference
                .child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Log.d(TAG, "onDataChange: "+dataSnapshot.getValue(User.class).toString());
                        if(dataSnapshot.hasChild(id)){
                            loggedInUser = (User) dataSnapshot.child(id).getValue(User.class);
                            LoginFragmentDirections.ActionLoginFragmentToMealsActivity action = LoginFragmentDirections.actionLoginFragmentToMealsActivity();
                            action.setLoggedinUser(loggedInUser);
                            Log.i(TAG, "onComplete: " + loggedInUser);
                            Navigation.findNavController(getView()).navigate(action);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public boolean isAdmin(String id) {

        mDatabaseReference.child("admins").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {


                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }
}
