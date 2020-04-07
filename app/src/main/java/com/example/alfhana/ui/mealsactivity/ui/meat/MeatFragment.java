package com.example.alfhana.ui.mealsactivity.ui.meat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.repository.MealRepository;
import com.example.alfhana.ui.mealsactivity.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MeatFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
    FirebaseRecyclerAdapter adapter;
    private static final String TAG = "MeatFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_meat, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_meat);

        Query q = databaseReference.child("meals").child("Meat");
        FirebaseRecyclerOptions<Meal> options =
                new FirebaseRecyclerOptions.Builder<Meal>()
                        .setQuery(q, Meal.class)
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .build();
        adapter = new FirebaseRecyclerAdapter<Meal, FoodViewHolder>(options) {
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meal_item, parent, false);

                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(FoodViewHolder holder, int position, Meal model) {
                // Bind the Chat object to the ChatHolder
                // ...
                Glide.with(getContext())
                        .load(model.getImageUri()).apply(new RequestOptions())
                        .into(holder.imgFood);
                holder.txtvFood.setText(model.getName());
                Log.i(TAG, "onBindViewHolder: " + model.getName());
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
