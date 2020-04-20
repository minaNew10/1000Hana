package com.example.alfhana.ui.mealsactivity.userfragments.cart;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.alfhana.data.model.Request;
import com.example.alfhana.data.model.User;
import com.example.alfhana.data.repository.UserRepository;
import com.example.alfhana.databinding.CartFragmentBinding;

import java.util.List;




public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private CartFragmentBinding mCartFragmentBinding;
    private List<Order> mCurrOrders;
    private CartAdapter mCartAdapter;
    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       mCartFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.cart_fragment,
               container,false);
        mCartFragmentBinding.listCart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
         mCartAdapter = new CartAdapter();
        mCartFragmentBinding.listCart.setAdapter(mCartAdapter);
        mCartFragmentBinding.setCart(this);
        return mCartFragmentBinding.getRoot();
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

                mCurrOrders = orders;
                mCartAdapter.setItems(mCurrOrders);
                mCartFragmentBinding.txtvTotal.setText(calculateTotal());
            }
        });
    }

    public void placeOrder(){
        User user = UserRepository.getInstance().getUser();
        final Request request = new Request(user.getPhone(),
                user.getDisplayName(),
                user.address,
                calculateTotal(),
                mCurrOrders
        );
        LiveData<Long> idLive = mViewModel.saveRequestInDatabase(getActivity(),request);

        idLive.observe(getActivity(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                request.setOrderId(aLong);
                extractRequestForWidget(request);
                mViewModel.saveRequestToServer(request);
            }
        });

        mViewModel.delOrders();
    }

    private void extractRequestForWidget(Request request) {
        StringBuilder sb = new StringBuilder();
        List<Order> orders = request.getOrders();
        for (int i = 0; i < orders.size() -1 ; i++) {
            Order order = orders.get(i);
            sb.append(order.productName + ", ");
        }
        sb.append(orders.get(orders.size()-1).productName + ".");
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_orders_shared_pref),sb.toString());
        editor.putString(getString(R.string.key_total_shared_pref),request.getTotal() + getString(R.string.pound));
        editor.commit();
    }

    private String calculateTotal(){
        int total = 0;
        for(Order order : mCurrOrders)
            total+= (Integer.valueOf(order.price)*Integer.valueOf(order.getQuantity()));
        return String.valueOf(total);
    }

    private void makeRequest() {
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle("One more step");
//        alertDialog.setMessage("Enter your address: ");
//
//        final EditText edtAddress = new EditText(getActivity());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        );
//        edtAddress.setLayoutParams(lp);
//        alertDialog.setView(edtAddress);
//        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alertDialog.show();

    }

}
