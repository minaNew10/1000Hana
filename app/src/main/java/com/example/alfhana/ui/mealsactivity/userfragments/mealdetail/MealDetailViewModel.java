package com.example.alfhana.ui.mealsactivity.userfragments.mealdetail;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.model.Order;
import com.example.alfhana.data.repository.OrderRepository;

public class MealDetailViewModel extends ViewModel {

    OrderRepository orderRepository;

    public MealDetailViewModel() {

    }

    public void insertOrder(Context context,Order order) {
        orderRepository = OrderRepository.getInstance();
        orderRepository.addOrder(context,order);
    }
}
