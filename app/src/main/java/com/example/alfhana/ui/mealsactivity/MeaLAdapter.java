package com.example.alfhana.ui.mealsactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.databinding.MealItemBinding;

import java.util.ArrayList;
import java.util.List;


public class MeaLAdapter extends RecyclerView.Adapter<MeaLAdapter.MealViewHolder> {
    List<Meal> items = new ArrayList<>();

    Context context;

    public MeaLAdapter() {

    }

    public void setItems(List<Meal> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public MeaLAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealItemBinding mealItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.meal_item,parent,false);
        return new MealViewHolder(mealItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal item = items.get(position);
        holder.mealItemBinding.setMeal(item);
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        private MealItemBinding mealItemBinding;
        public MealViewHolder(@NonNull MealItemBinding mealItemBinding) {
            super(mealItemBinding.getRoot());
            this.mealItemBinding = mealItemBinding;
        }
    }
}
