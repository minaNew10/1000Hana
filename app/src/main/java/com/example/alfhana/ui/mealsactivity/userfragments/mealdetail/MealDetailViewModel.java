package com.example.alfhana.ui.mealsactivity.userfragments.mealdetail;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.repository.OrderRepository;

public class MealDetailViewModel extends ViewModel {

    Context context;
    OrderRepository orderRepository;

    public MealDetailViewModel() {
        this.context = context;
    }
}
