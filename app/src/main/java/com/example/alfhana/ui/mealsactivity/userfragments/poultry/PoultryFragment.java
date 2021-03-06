package com.example.alfhana.ui.mealsactivity.userfragments.poultry;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.ui.mealsactivity.MeaLAdapter;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;


public class PoultryFragment extends Fragment implements MeaLAdapter.OnMealClickListener{
    MeaLAdapter mAdapter;

    PoultryViewModel mViewModel;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Meal> meals = new ArrayList<>();

    private PoultryViewModel poultryViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meat, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view_meat);
        mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mViewModel = ViewModelProviders.of(this).get(PoultryViewModel.class);
        mAdapter = new MeaLAdapter(getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
        mViewModel.getMeals().observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            meals.clear();

                            Meal meal;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                meal = child.getValue(Meal.class);


                                meals.add(meal);
                            }
                            mAdapter.setItems(meals);
                        }

                    }
                }
        );




        return root;
    }

    @Override
    public void onClick(Meal meal) {
        Bundle b = new Bundle();
        b.putParcelable(getString(R.string.key_meal),meal);
        NavOptions navOptions = new NavOptions.Builder().build();
        Navigation.findNavController(getView()).navigate(R.id.mealDetailFragment,b,navOptions);

    }
}
