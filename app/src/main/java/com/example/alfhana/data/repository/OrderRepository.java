package com.example.alfhana.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.alfhana.data.model.Order;
import com.example.alfhana.database.AppDatabase;

import java.util.List;

public class OrderRepository {
    private static AppDatabase appDatabase;
    private static volatile OrderRepository instance;
    private static final String TAG = "MealRepository";
    private OrderRepository(){

    }
    public static OrderRepository getInstance(){
        if(instance == null){
            instance = new OrderRepository();
        }
        return instance;
    }

    public void addOrder(Context context,Order order){
        appDatabase = AppDatabase.getInstance(context);
        appDatabase.orderDao().insert(order);
    }
    public LiveData<List<Order>> getOrders(Context context){
        appDatabase = AppDatabase.getInstance(context);
        return appDatabase.orderDao().getAllOrders();
    }
}
