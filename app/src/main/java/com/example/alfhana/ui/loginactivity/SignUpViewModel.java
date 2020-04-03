package com.example.alfhana.ui.loginactivity;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.UserRepository;
import com.example.alfhana.data.model.User;

public class SignUpViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();

    public SignUpViewModel() {

    }

    public MutableLiveData<Boolean> register(String name,String psswrd) {
        MutableLiveData<Boolean> isRegistered =
                userRepository.register(name,psswrd);
        return isRegistered;
    }
    public MutableLiveData<Boolean> saveUser(User user) {
        MutableLiveData<Boolean> isSaved =
                userRepository.saveUserInDatabase(user);
        return isSaved;
    }


    public String storeImage(String imageFileName, Uri mImageUri) {
        return userRepository.storeImage(imageFileName,mImageUri);
    }
}
