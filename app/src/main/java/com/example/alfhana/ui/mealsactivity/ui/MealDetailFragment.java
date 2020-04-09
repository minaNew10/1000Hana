package com.example.alfhana.ui.mealsactivity.ui;

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
import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.databinding.MealDetailFragmentBinding;

public class MealDetailFragment extends Fragment {

    private MealDetailViewModel mViewModel;
    MealDetailFragmentBinding mealDetailFragmentBinding;
    public static MealDetailFragment newInstance() {
        return new MealDetailFragment();
    }

    private static final String TAG = "MealDetailFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mealDetailFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.meal_detail_fragment,container,false);
//        MealDetailFragmentArgs arg = MealDetailFragmentArgs.fromBundle(getArguments());
//        Meal meal = arg.getMeal();
        Bundle b= getArguments();
        Meal m = b.getParcelable("meal");

        mealDetailFragmentBinding.setMeal(m);
        return mealDetailFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MealDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}
