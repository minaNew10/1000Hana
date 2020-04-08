package com.example.alfhana.ui.mealsactivity.ui.meat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;

import java.util.ArrayList;
import java.util.List;


public class MeatAdapter extends RecyclerView.Adapter<MeatAdapter.MealViewHolder> {
    List<Meal> items = new ArrayList<>();

    Context context;

    public MeatAdapter() {

    }

    public void setItems(List<Meal> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public MeatAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal item = items.get(position);
        Glide.with(context)
                .load(item.getImageUri()).apply(new RequestOptions())
                .into(holder.imgFood);
        holder.txtvFood.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgFood;
        public TextView txtvFood;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgv_food);
            txtvFood= itemView.findViewById(R.id.txtv_food_name);
        }


    }
}
