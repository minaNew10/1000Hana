package com.example.alfhana.ui.mealsactivity.ui.meat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.ui.mealsactivity.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MeatFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
    FirebaseRecyclerAdapter mAdapter;
    private static final String TAG = "MeatFragment";
    MeatViewModel mViewModel;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private int mRvPositionIndex;
    private int mRvTopView;
    public static final String RV_POS_INDEX = "pos index";
    public static final String RV_TOP_VIEW= "top view";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_meat, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view_meat);

        Query q = databaseReference.child("meals").child("Meat");
        FirebaseRecyclerOptions<Meal> options =
                new FirebaseRecyclerOptions.Builder<Meal>()
                        .setQuery(q, Meal.class)
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<Meal, FoodViewHolder>(options) {
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        if (savedInstanceState != null) {

            mRvPositionIndex = savedInstanceState.getInt(RV_POS_INDEX);
            mRvTopView = savedInstanceState.getInt(RV_TOP_VIEW);

            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(mRvPositionIndex, mRvTopView);
                }
            });
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MeatViewModel.class);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mRvPositionIndex = linearLayoutManager.findFirstVisibleItemPosition();
        View startView = mRecyclerView.getChildAt(0);
        mRvTopView = (startView == null) ? 0 : (startView.getTop() - mRecyclerView.getPaddingTop());
        outState.putInt(RV_POS_INDEX, mRvPositionIndex);
        outState.putInt(RV_TOP_VIEW, mRvTopView);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
