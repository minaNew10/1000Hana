package com.example.alfhana.ui.mealsactivity.adminonlyfragments;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;

public class AddMealViewModel extends ViewModel {
    private MealRepository mealRepository= MealRepository.getInstance();
    private MutableLiveData<Boolean> isSaved;
    private MutableLiveData<String> storedUri;

    public MutableLiveData<Boolean> saveMeal(Meal meal) {
        isSaved = mealRepository.saveMeal(meal);
        return isSaved;
    }


    public MutableLiveData<String> storeImage(String imageFileName, Uri mImageUri) {
        storedUri = mealRepository.storeImage(imageFileName,mImageUri);
        return storedUri;
    }
}
