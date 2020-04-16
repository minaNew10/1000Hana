package com.example.alfhana.ui.loginactivity.loginfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.alfhana.R;
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.FragmentLoginBinding;
import com.example.alfhana.ui.loginactivity.ViewModelsFactory;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment
        implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "login";
    LoginViewModel mLoginViewModel;
    FragmentLoginBinding mLoginBinding;
    MutableLiveData<Boolean> mLoginSuccessfulMutableLiveData;
    MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();
    NavController mNavController;
    User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mNavController = NavHostFragment.findNavController(LoginFragment.this);

        // Inflate the layout for this fragment
        mLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mLoginBinding.btnLogin.setEnabled(true);
        View v = mLoginBinding.getRoot();
        mLoginBinding.setSubmit(this);

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
                mLoginViewModel.loginDataChanged(mLoginBinding.etxtEmailLogin.getText().toString().trim(),
                        mLoginBinding.etxtPsswrdLogin.getText().toString().trim());
            }
        };
        mLoginBinding.etxtEmailLogin.addTextChangedListener(afterTextChangedListener);
        mLoginBinding.etxtPsswrdLogin.addTextChangedListener(afterTextChangedListener);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    public void goToSignUp() {
        mNavController.setGraph(R.navigation.nav_graph);
        mNavController.navigate(R.id.action_loginFragment_to_signUpFragment);
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
                    mLoginBinding.btnLogin.setEnabled(true);
                    mLoginBinding.btnLogin.setAlpha(1);
                    mLoginBinding.btnLogin.setElevation(4f);
                }

                if (loginFormState.getUsernameError() != null) {
                    mLoginBinding.btnLogin.setEnabled(false);
                    mLoginBinding.btnLogin.setAlpha(0.5f);
                    mLoginBinding.etxtEmailLogin.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    mLoginBinding.btnLogin.setEnabled(false);
                    mLoginBinding.btnLogin.setAlpha(0.5f);
                    Integer pssWrdErr = loginFormState.getPasswordError();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    mLoginBinding.etxtPsswrdLogin.setError(getString(pssWrdErr));

                }

            }
        });

        mLoginViewModel.getUserMutableLiveData();
        mUserMutableLiveData.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.i(TAG, "onChanged: " + user);
                if (user != null) {
//                    LoginFragmentDirections.ActionLoginFragmentToMealsActivity action = LoginFragmentDirections.actionLoginFragmentToMealsActivity();
//                    action.setLoggedinUser(user);
//                    Navigation.findNavController(getView()).navigate(action);
                    mLoginBinding.progressBarLogin.setVisibility(View.INVISIBLE);
                    mUser = user;
                    Bundle b = new Bundle();
                    b.putParcelable(getString(R.string.user_key),user);
                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.nav_graph,true).build();
                    Navigation.findNavController(getView()).navigate(R.id.mealsActivity,b,navOptions);
                }
            }
        });


    }

    public void login() {
        mLoginBinding.btnLogin.setEnabled(false);
        mLoginBinding.progressBarLogin.setVisibility(View.VISIBLE);
        mLoginSuccessfulMutableLiveData =
                mLoginViewModel.login(mLoginBinding.etxtEmailLogin.getEditableText().toString().trim(),
                        mLoginBinding.etxtLayoutPsswrdLogin.getEditText().getText().toString());

        mLoginSuccessfulMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    mLoginViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            mUserMutableLiveData.setValue(user);
                        }
                    });

                }else {
                    mLoginBinding.progressBarLogin.setVisibility(View.INVISIBLE);
                    mLoginViewModel.getErrLoginMsg().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i(TAG, "onChanged: " + s);
                            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mLoginBinding.btnLogin.setEnabled(isSignedIn());

    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
