package com.example.alfhana.ui.mealsactivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.repository.UserRepository;

public class MealsActivityViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private LiveData<Boolean> isUserAdmin;
    public MealsActivityViewModel() {
    }

    public LiveData<Boolean> checkUser(){
       String id =  userRepository.getFirebaseUser().getUid();
        if(isUserAdmin == null){
            isUserAdmin = userRepository.isAdmin(id);
        }
        return isUserAdmin;
    }
    public void logout(){
        userRepository.logOut();
    }
}
