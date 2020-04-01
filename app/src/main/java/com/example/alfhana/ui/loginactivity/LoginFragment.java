package com.example.alfhana.ui.loginactivity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    LoginViewModel loginViewModel;
    FragmentLoginBinding loginBinding;
    MutableLiveData<Boolean> loginSuccessful;
    MutableLiveData<User> userMutableLiveData;
    NavController navController;

    User loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(LoginFragment.this);

        setupViewModel();
        // Inflate the layout for this fragment
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        View v = loginBinding.getRoot();
        loginBinding.setSubmit(this);

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
        loginViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                loggedInUser = user;
            }
        });
    }


    public void goToSignUp() {
        navController.navigate(R.id.action_loginFragment_to_signUpFragment);
    }

    private void setupViewModel() {
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
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

    }

    public void login() {
        loginSuccessful =
        loginViewModel.login(loginBinding.etxtEmailLogin.getEditableText().toString().trim(),
                loginBinding.etxtLayoutPsswrdLogin.getEditText().getText().toString());
        loginSuccessful.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    LoginFragmentDirections.ActionLoginFragmentToMealsActivity action = LoginFragmentDirections.actionLoginFragmentToMealsActivity();
                    action.setLoggedinUser(loggedInUser);
                    Navigation.findNavController(getView()).navigate(action);
                }
            }
        });
    }
}
