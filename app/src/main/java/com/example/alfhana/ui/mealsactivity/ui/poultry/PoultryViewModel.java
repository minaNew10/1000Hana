package com.example.alfhana.ui.mealsactivity.ui.poultry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;
import com.example.alfhana.utils.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;

public class PoultryViewModel extends ViewModel {

    MealRepository mealRepository ;
    FirebaseQueryLiveData mealsList;
    public PoultryViewModel() {
        mealRepository = MealRepository.getInstance();
        mealsList = mealRepository.getMeals(Meal.Category.POULTRY);
    }



    public LiveData<DataSnapshot> getMeals() {
        return mealsList;
    }
}