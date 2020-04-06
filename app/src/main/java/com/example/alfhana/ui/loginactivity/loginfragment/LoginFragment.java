package com.example.alfhana.ui.loginactivity.loginfragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.FragmentLoginBinding;
import com.example.alfhana.ui.loginactivity.ViewModelsFactory;


public class LoginFragment extends Fragment {

    private static final String TAG = "login";
    LoginViewModel mLoginViewModel;
    FragmentLoginBinding loginBinding;
    MutableLiveData<Boolean> loginSuccessful;
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    NavController navController;
    User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navController = NavHostFragment.findNavController(LoginFragment.this);


        // Inflate the layout for this fragment
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        loginBinding.btnLogin.setEnabled(true);
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
                mLoginViewModel.loginDataChanged(loginBinding.etxtEmailLogin.getText().toString().trim(),
                        loginBinding.etxtPsswrdLogin.getText().toString().trim());
            }
        };
        loginBinding.etxtEmailLogin.addTextChangedListener(afterTextChangedListener);
        loginBinding.etxtPsswrdLogin.addTextChangedListener(afterTextChangedListener);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    public void goToSignUp() {
        navController.navigate(R.id.action_loginFragment_to_signUpFragment);
    }

    private void setupViewModel() {
        mLoginViewModel = ViewModelProviders.of(this, new ViewModelsFactory())
                .get(LoginViewModel.class);
        mLoginViewModel.getLoginFormState().observe(this.getActivity(), new Observer<LoginFormState>() {
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


        userMutableLiveData.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.i(TAG, "onChanged: " + user);
                if (user != null) {
//                    LoginFragmentDirections.ActionLoginFragmentToMealsActivity action = LoginFragmentDirections.actionLoginFragmentToMealsActivity();
//                    action.setLoggedinUser(user);
//                    Navigation.findNavController(getView()).navigate(action);
                    Bundle b = new Bundle();
                    b.putParcelable("loggedin_user",user);
                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.nav_graph,true).build();
                    Navigation.findNavController(getView()).navigate(R.id.mealsActivity,b,navOptions);
                }
            }
        });


    }

    public void login() {
        loginSuccessful =
                mLoginViewModel.login(loginBinding.etxtEmailLogin.getEditableText().toString().trim(),
                        loginBinding.etxtLayoutPsswrdLogin.getEditText().getText().toString());

        loginSuccessful.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i(TAG, "onChanged: " + aBoolean);
                if (aBoolean) {
                    mLoginViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            Log.i(TAG, "onChanged: nested"+ user);
                            userMutableLiveData.setValue(user);
                        }
                    });

                }
            }
        });


    }
}
