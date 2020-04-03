package com.example.alfhana.ui.mealsactivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.alfhana.R;
import com.example.alfhana.data.UserRepository;
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.ActivityMealsBinding;
import com.example.alfhana.databinding.NavHeaderMealsBinding;
import com.example.alfhana.ui.loginactivity.LoginViewModel;
import com.example.alfhana.ui.loginactivity.ViewModelsFactory;
import com.example.alfhana.ui.mealsactivity.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MealsActivity extends AppCompatActivity {
    private static final String TAG = "MealsActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private UserRepository userRepository = UserRepository.getInstance();
    MealsActivityViewModel mMealsActivityViewModel;
    private MutableLiveData<Boolean> isUserAdmin = new MutableLiveData<>();
    ActivityMealsBinding activityMealsBinding;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        Bundle b = getIntent().getExtras();
        user = b.getParcelable("loggedin_user");
        Log.i(TAG, "onCreate: " + user.getEmail());
        setupViewModel();

        isUserAdmin.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
              if(aBoolean){
                    Toast.makeText(MealsActivity.this,"hello admin",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MealsActivity.this,"hello user",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        NavHeaderMealsBinding navHeaderMealsBinding = NavHeaderMealsBinding.bind(header);
        navHeaderMealsBinding.setUser(user);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.meals_navigation,b);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupViewModel() {
        mMealsActivityViewModel = ViewModelProviders.of(this, new ViewModelsFactory())
                .get(MealsActivityViewModel.class);
        mMealsActivityViewModel.checkUser().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isUserAdmin.setValue(aBoolean);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                userRepository.logOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        super.onBackPressed();
    }
}
