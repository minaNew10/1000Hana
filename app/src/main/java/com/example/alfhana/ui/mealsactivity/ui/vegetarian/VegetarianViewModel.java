package com.example.alfhana.ui.mealsactivity.ui.vegetarian;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VegetarianViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VegetarianViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}