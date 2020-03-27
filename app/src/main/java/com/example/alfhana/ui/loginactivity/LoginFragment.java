package com.example.alfhana.ui.loginactivity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.alfhana.R;
import com.example.alfhana.data.UserRepository;
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
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

    private FirebaseDatabase mFirebaseDatabase;

    UserRepository userRepository;
    private static final int RC_START_SIGN_UP = 1;
    User LoggedInUser;
    LoginViewModel loginViewModel;
    FragmentLoginBinding loginBinding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    MutableLiveData<Boolean> loginSuccessful;
    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> isAdmin;
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
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        userRepository = UserRepository.getInstance(this.getActivity());
        View v = loginBinding.getRoot();
        loginBinding.setSubmit(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

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
//        loggedInUser = userRepository.CheckLoginState();
        if (loggedInUser == null)
            Log.d(TAG, "register now: ");
        else
            Log.d(TAG, "session exist: ");
        if (getArguments() != null) {
            LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
            if (args.getUser() != null) {
                loggedInUser = args.getUser();
                loginBinding.etxtEmailLogin.setText(loggedInUser.getEmail());

            }
        }
    }


    public void goToSignUp() {
        NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_signUpFragment);
    }

    public void login() {
        userMutableLiveData =
                userRepository.login(loginBinding.etxtEmailLogin.getEditableText().toString().trim(),
                        loginBinding.etxtPsswrdLogin.getEditableText().toString());

        userMutableLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    LoginFragmentDirections.ActionLoginFragmentToMealsActivity action = LoginFragmentDirections.actionLoginFragmentToMealsActivity();
                    action.setLoggedinUser(user);
                    Navigation.findNavController(getView()).navigate(action);
                }
            }
        });
//        isAdmin.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(aBoolean){
//                    LoginFragmentDirections.ActionLoginFragmentToMealsActivity action = LoginFragmentDirections.actionLoginFragmentToMealsActivity();
//                    action.setLoggedinUser(null);
//                    Navigation.findNavController(getView()).navigate(action);
//                }
//
//            }
//        });

    }
}
