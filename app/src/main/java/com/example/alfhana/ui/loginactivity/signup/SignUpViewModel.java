package com.example.alfhana.ui.loginactivity.signup;

import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.R;
import com.example.alfhana.data.repository.UserRepository;
import com.example.alfhana.data.model.User;


public class SignUpViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> isRegistered;
    private MutableLiveData<Boolean> isSaved;
    private MutableLiveData<String> storedUri;
    private MutableLiveData<User> userInput;
    private MutableLiveData<String> psswrd;
    private Uri userImageUri;
    private String imageFileName;
    private MutableLiveData<SignUpFormState> signUpFormStateMutableLiveData = new MutableLiveData<>();
    public SignUpViewModel() {
        userInput = new MutableLiveData<>();
        psswrd = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> register(String name,String psswrd) {
        isRegistered = userRepository.register(name,psswrd);
        return isRegistered;
    }
    public MutableLiveData<Boolean> saveUser(User user) {
        isSaved = userRepository.saveUserInDatabase(user);
        return isSaved;
    }


    public MutableLiveData<String> storeImage(String imageFileName, Uri mImageUri) {
        storedUri = userRepository.storeImage(imageFileName,mImageUri);
        return storedUri;
    }



    public void signUpDataChanged(String name, String password,String email) {
        if (!isEmailValid(email)) {
            signUpFormStateMutableLiveData.setValue(new SignUpFormState( null, null,R.string.invalid_email));
        } else if (!isPasswordValid(password)) {
            signUpFormStateMutableLiveData.setValue(new SignUpFormState(null, R.string.invalid_password,null));
        } else if (!isUserNameValid(name)) {
            signUpFormStateMutableLiveData.setValue(new SignUpFormState(R.string.user_name_err,null,null));
        } else {
            signUpFormStateMutableLiveData.setValue(new SignUpFormState(true));
        }
    }

    private boolean isUserNameValid(String name) {
        if(name.length() < 4)
            return false;
        return true;
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

    public MutableLiveData<SignUpFormState> getSignUpFormState() {
        return signUpFormStateMutableLiveData;
    }

    public void setImageUri(Uri mImageUri) {
        this.userImageUri = mImageUri;
    }
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Uri getUserImageUri() {
        return userImageUri;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public MutableLiveData<User> getUserInput() {
        return userInput;
    }
    public void setUserInput(User user){
        userInput.setValue(user);
    }

    public MutableLiveData<String> getPsswrd() {
        return psswrd;
    }

    public void setPsswrd(String psswrd) {
        this.psswrd.setValue(psswrd);
    }

    public MutableLiveData<String> getErrRegisterMsg() {
        return userRepository.getRegisterErrMsg();
    }
}
