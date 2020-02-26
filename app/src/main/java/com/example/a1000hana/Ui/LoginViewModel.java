package com.example.a1000hana.Ui;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a1000hana.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
//    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
//    private LoginRepository loginRepository;

//    LoginViewModel(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

//    LiveData<LoginResult> getLoginResult() {
//        return loginResult;
//    }

//    public void login(FirebaseAuth firebaseAuth,String username, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.login(firebaseAuth,username, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (!email.isEmpty()) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
