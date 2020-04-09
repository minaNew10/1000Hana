package com.example.alfhana.ui.mealsactivity.ui.meat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.ui.mealsactivity.MeaLAdapter;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;


public class MeatFragment extends Fragment {

    MeaLAdapter mAdapter;
    private static final String TAG = "MealRepository";
    MeatViewModel mViewModel;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Meal> meals = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meat, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view_meat);
        mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mViewModel = ViewModelProviders.of(this).get(MeatViewModel.class);
        mAdapter = new MeaLAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mViewModel.getMeals().observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            meals.clear();

                            Meal meal;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                meal = child.getValue(Meal.class);
                                Log.i(TAG, "onDataChange fragment: " + meal.getName());

                                meals.add(meal);
                            }
                            mAdapter.setItems(meals);
                        }

                    }
                }
        );
        return root;
    }
}
