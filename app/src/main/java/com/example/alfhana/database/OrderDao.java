package com.example.alfhana.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.alfhana.data.model.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Query("DELETE FROM `order`")
    void deleteAll();

    @Query("SELECT * FROM 'order'")
    LiveData<List<Order>> getAllOrdersLiveData();

    @Query("SELECT * FROM 'order'")
    List<Order> getAllOrdersInList();

    @Query("DELETE FROM '" + "order" + "' WHERE "+ "OrderId"  + " = :id")
    int deleteById(long id);
}
