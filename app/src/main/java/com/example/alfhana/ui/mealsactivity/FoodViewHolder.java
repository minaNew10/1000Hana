package com.example.alfhana.ui.mealsactivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alfhana.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView imgFood;
    public TextView txtvFood;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        imgFood = itemView.findViewById(R.id.imgv_food);
        txtvFood= itemView.findViewById(R.id.txtv_food_name);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
//        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
    public interface ItemClickListener {
        void onClick(View view,int postion,boolean isLongClick);
    }
}
