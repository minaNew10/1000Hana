package com.example.alfhana.ui.mealsactivity.ui.vegetarian;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;
import com.example.alfhana.utils.FirebaseQueryLiveData;

public class VegetarianViewModel extends ViewModel {
    MealRepository mealRepository ;
    FirebaseQueryLiveData mealsList;
    public VegetarianViewModel() {
        mealRepository = MealRepository.getInstance();
        mealsList = mealRepository.getMeals(Meal.Category.VEGETARIAN);
    }
    public FirebaseQueryLiveData getMeals() {
        return mealsList;
    }

}