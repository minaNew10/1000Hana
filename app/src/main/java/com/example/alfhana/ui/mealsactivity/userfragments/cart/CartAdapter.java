package com.example.alfhana.ui.mealsactivity.userfragments.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Order;
import com.example.alfhana.databinding.CartItemBinding;
import com.example.alfhana.ui.mealsactivity.MeaLAdapter;

import java.util.ArrayList;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Order> items = new ArrayList<>();

    onOrderClickListener onRecyclerItemClickListener;
    Context context;

    public interface onOrderClickListener {
        void onClick(Order currItem);
    }

    public CartAdapter() {

    }

    public void setItems(List<Order> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public CartAdapter(Context context, onOrderClickListener listener) {
        this.context = context;
        this.onRecyclerItemClickListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding cartItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.cart_item,parent,false);

        return new CartViewHolder(cartItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Order item = items.get(position);
        holder.cartItemBinding.setOrder(item);
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CartItemBinding cartItemBinding;
        public CartViewHolder(@NonNull CartItemBinding cartItemBinding) {
            super(cartItemBinding.getRoot());
            this.cartItemBinding = cartItemBinding;
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecyclerItemClickListener.onClick(items.get(getAdapterPosition()));
        }
    }
}
