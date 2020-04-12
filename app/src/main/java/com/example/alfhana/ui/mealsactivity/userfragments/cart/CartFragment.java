package com.example.alfhana.ui.mealsactivity.userfragments.cart;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Order;
import com.example.alfhana.databinding.CartFragmentBinding;

import java.util.List;




public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    private CartViewModel mViewModel;
    CartFragmentBinding cartFragmentBinding;
    List<Order> currOrders;
    CartAdapter cartAdapter;
    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       cartFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.cart_fragment,
               container,false);
        cartFragmentBinding.listCart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
         cartAdapter = new CartAdapter();
        cartFragmentBinding.listCart.setAdapter(cartAdapter);
        cartFragmentBinding.setCart(this);
        return cartFragmentBinding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();
    }

    private void setupViewModel(){
        mViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        mViewModel.getOrders(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                Log.i(TAG, "onChanged: "+ orders.size());
                currOrders = orders;
                cartAdapter.setItems(currOrders);
            }
        });
    }

    public void startMap(){
//        Intent intent = new Intent(getActivity(), MapsActivity.class);
//        startActivityForResult(intent,11);
    }
}
