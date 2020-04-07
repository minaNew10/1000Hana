package com.example.alfhana.ui.mealsactivity.ui.meat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeatViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MeatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}