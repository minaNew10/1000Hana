package com.example.alfhana.database;


import androidx.room.TypeConverter;

import com.example.alfhana.data.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {
    @TypeConverter
    public String fromOrdersList(List<Order> orders) {
        if (orders == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Order>>() {}.getType();
        String json = gson.toJson(orders, type);
        return json;
    }

    @TypeConverter
    public List<Order> toOrder(String orderString) {
        if (orderString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Order>>() {}.getType();
        List<Order> countryLangList = gson.fromJson(orderString, type);
        return countryLangList;
    }
}
