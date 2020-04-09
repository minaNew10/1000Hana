package com.example.alfhana.ui.mealsactivity.ui.meat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;
import com.google.firebase.database.DataSnapshot;

public class MeatViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private RecyclerView.LayoutManager layoutManager;
    public MeatViewModel() {

    }

//    public LiveData<DataSnapshot> getMeals() {
//        return MealRepository.getInstance().getMeals(Meal.Category.MEAT);
//    }

}