package com.example.alfhana.data.pagingdatasource;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.alfhana.data.model.Meal;

public class MealsListDataSource extends PageKeyedDataSource<Integer, Meal> {
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Meal> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Meal> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Meal> callback) {

    }
}
