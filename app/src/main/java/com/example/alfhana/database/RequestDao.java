package com.example.alfhana.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.alfhana.data.model.Request;

import java.util.List;

@Dao
public interface RequestDao {


    @Insert
    long insert(Request request);

    @Query("SELECT * FROM 'request'")
    LiveData<List<Request>> getAllRequestsLiveData();

    @Query("SELECT * FROM 'request'")
    List<Request> getAllrequestsInList();

}
