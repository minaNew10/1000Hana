package com.example.alfhana.ui.loginactivity.signup;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.repository.UserRepository;
import com.example.alfhana.data.model.User;

public class SignUpViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> isRegistered;
    private MutableLiveData<Boolean> isSaved;
    private MutableLiveData<String> storedUri;
    public SignUpViewModel() {

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
}
