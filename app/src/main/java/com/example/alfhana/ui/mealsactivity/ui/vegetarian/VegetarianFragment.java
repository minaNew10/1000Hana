package com.example.alfhana.ui.mealsactivity.ui.vegetarian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
;import com.example.alfhana.R;

public class VegetarianFragment extends Fragment {

    private VegetarianViewModel vegetarianViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vegetarianViewModel =
                ViewModelProviders.of(this).get(VegetarianViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vegetarian, container, false);

        return root;
    }
}
