package com.example.alfhana.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.alfhana.data.model.Order;
import com.example.alfhana.database.AppDatabase;
import com.example.alfhana.utils.AppExecutors;

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

    public void addOrder(Context context,final Order order){
        appDatabase = AppDatabase.getInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
            appDatabase.orderDao().insert(order);
            }
        });
    }
    public LiveData<List<Order>> getOrders(Context context){
        appDatabase = AppDatabase.getInstance(context);
        return appDatabase.orderDao().getAllOrdersLiveData();
    }

    public void delOrder(final Order order){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.orderDao().deleteById(order.getOrderId());
            }
        });
    }
    public void delAllOrders(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.orderDao().deleteAll();
            }
        });
    }
}
