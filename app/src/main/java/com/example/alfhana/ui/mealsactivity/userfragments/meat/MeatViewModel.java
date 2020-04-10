package com.example.alfhana.ui.mealsactivity.userfragments.meat;

import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;
import com.example.alfhana.utils.FirebaseQueryLiveData;

public class MeatViewModel extends ViewModel {
    MealRepository mealRepository ;
    FirebaseQueryLiveData mealsList;
    public MeatViewModel() {
        mealRepository = MealRepository.getInstance();
        mealsList = mealRepository.getMeals(Meal.Category.MEAT);
    }
    public FirebaseQueryLiveData getMeals() {
        return mealsList;
    }
}