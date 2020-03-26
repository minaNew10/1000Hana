package com.example.alfhana.ui.loginactivity;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.R;
import com.example.alfhana.data.UserRepository;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private UserRepository userRepository;
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

//    LoginViewModel(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

//    LiveData<LoginResult> getLoginResult() {
//        return loginResult;
//    }

//    public Boolean login(String username, String password) {
//        // can be launched in a separate asynchronous job
//        Boolean result = userRepository.login(username, password);
//
////        if (result instanceof Result.Success) {
////            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
////            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
////        } else {
////            loginResult.setValue(new LoginResult(R.string.login_failed));
////        }
//        return  result;
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
