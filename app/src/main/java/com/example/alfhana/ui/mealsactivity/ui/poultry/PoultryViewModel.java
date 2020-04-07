package com.example.alfhana.ui.mealsactivity.ui.poultry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PoultryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PoultryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}