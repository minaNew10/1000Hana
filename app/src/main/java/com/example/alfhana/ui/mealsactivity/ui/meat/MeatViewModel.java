package com.example.alfhana.ui.mealsactivity.ui.meat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;
import com.example.alfhana.utils.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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