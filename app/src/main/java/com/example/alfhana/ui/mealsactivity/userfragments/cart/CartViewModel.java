package com.example.alfhana.ui.mealsactivity.userfragments.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alfhana.data.model.Order;
import com.example.alfhana.data.model.Request;
import com.example.alfhana.data.repository.OrderRepository;
import com.example.alfhana.data.repository.RequestRepository;

import java.util.List;

public class CartViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private OrderRepository orderRepository;
    private RequestRepository requestRepository;
    private LiveData<List<Order>> orders;
    public CartViewModel(){
        orderRepository = OrderRepository.getInstance();
        requestRepository = RequestRepository.getInstance();
    }

    LiveData<List<Order>> getOrders(Context context){
        orders = orderRepository.getOrders(context);
        return orders;
    }
    LiveData<Boolean> saveRequest(Request request){
       return requestRepository.placeOrder(request);
    }
}
