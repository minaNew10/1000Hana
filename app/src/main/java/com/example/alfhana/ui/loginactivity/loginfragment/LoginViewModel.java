package com.example.alfhana.ui.loginactivity.loginfragment;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.R;
import com.example.alfhana.data.repository.UserRepository;
import com.example.alfhana.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> loginResult;
    private static final String TAG = "login";
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public LoginViewModel() {

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    MutableLiveData<Boolean> login(String email,String psswrd){
        loginResult = userRepository.login(email, psswrd);
        return loginResult;

    }
    MutableLiveData<User> getUserMutableLiveData(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.i(TAG, "getUser: repo " + user);
                userMutableLiveData = userRepository.retrieveUserFromDatabase(user.getUid());
            } else {
                Log.i(TAG, "getUser: repo " + user);
                userMutableLiveData = new MutableLiveData<>();
                userMutableLiveData.postValue(null);
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
