package com.example.alfhana.ui.loginactivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.alfhana.data.UserRepository;
import com.example.alfhana.ui.mealsactivity.MealsActivityViewModel;


/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class ViewModelsFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        } else if(modelClass.isAssignableFrom(MealsActivityViewModel.class)){
            return (T) new MealsActivityViewModel();
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
