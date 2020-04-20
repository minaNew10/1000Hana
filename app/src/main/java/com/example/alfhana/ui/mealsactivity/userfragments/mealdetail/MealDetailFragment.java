package com.example.alfhana.ui.mealsactivity.userfragments.mealdetail;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.model.Order;
import com.example.alfhana.database.AppDatabase;
import com.example.alfhana.databinding.MealDetailFragmentBinding;

public class MealDetailFragment extends Fragment {
    AppDatabase appDatabase;
    private MealDetailViewModel mViewModel;
    MealDetailFragmentBinding mealDetailFragmentBinding;
    public static MealDetailFragment newInstance() {
        return new MealDetailFragment();
    }
    Meal currMeal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        appDatabase = AppDatabase.getInstance(getActivity());
        mealDetailFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.meal_detail_fragment,container,false);
        Bundle b= getArguments();
        currMeal = b.getParcelable(getString(R.string.meal_key));
        mealDetailFragmentBinding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrder();
            }
        });
        mealDetailFragmentBinding.setMeal(currMeal);
        mealDetailFragmentBinding.setMealFragment(this);
        return mealDetailFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MealDetailViewModel.class);

    }

    public void addOrder(){
        Order order = new Order(currMeal.getId(),currMeal.getName(),currMeal.getCategory(),mealDetailFragmentBinding.numberButton.getNumber()+"",String.valueOf(currMeal.getPrice()));
        mViewModel.insertOrder(getActivity(),order);
    }

}
