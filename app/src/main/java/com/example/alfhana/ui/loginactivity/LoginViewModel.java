package com.example.alfhana.ui.loginactivity;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.R;
import com.example.alfhana.data.UserRepository;
import com.example.alfhana.data.model.User;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Boolean> loginResult;

    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    LoginViewModel() {

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    MutableLiveData<Boolean> login(String email,String psswrd){
        if(loginResult == null)
            loginResult = userRepository.login(email,psswrd);
        return loginResult;
    }
    MutableLiveData<User> getUserMutableLiveData(){
        if(userMutableLiveData == null){
            userMutableLiveData = userRepository.getUser();
        }
        return userMutableLiveData;
    }
    void logout(){
        userRepository.logOut();
    }
    public void setUserMutableLiveData(User user){
        userMutableLiveData.setValue(user);
    }
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
