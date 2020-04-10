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

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM 'order'")
    LiveData<List<Order>> getAllOrders();

    @Query("DELETE FROM '" + "order" + "' WHERE "+ "OrderId"  + " = :id")
    int deleteById(long id);
}
