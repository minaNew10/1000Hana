package com.example.alfhana.ui.mealsactivity.adminonlyfragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Meal;
import com.example.alfhana.databinding.FragmentAddMealsBinding;
import com.example.alfhana.utils.ImageUtils;

import java.io.File;


public class AddMealsFragment extends Fragment {
    public static final int RC_CAMERA_INTENT = 103;
    public static final int RC_PERMISSIONSETTINGS = 104;
    public static final int RC_GALLERY = 107;
    private static final int RC_PERMESIONS = 101;
    public static final int RC_CHOOSER_INTENT = 102;
    private AddMealViewModel mViewModel;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"};
    MutableLiveData<Boolean> saveSuccessful;
    FragmentAddMealsBinding fragmentAddMealsBinding;
    ImageUtils imageUtils;
    private String imageFileName;
    private static final String TAG = "Add";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAddMealsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_meals,container,false);
        fragmentAddMealsBinding.setMealFragment(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentAddMealsBinding.spinnerCatSaveMeal.setAdapter(adapter);
        return fragmentAddMealsBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageUtils = new ImageUtils(getActivity());
        mViewModel = ViewModelProviders.of(this).get(AddMealViewModel.class);

    }

    public void askPermissions() {

        if (imageUtils.allPermissionsGranted()) {
            Log.i(TAG, "askPermissions: ");
            Intent intent = imageUtils.createChooserIntent();
            startActivityForResult(intent,RC_CHOOSER_INTENT);
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, RC_PERMESIONS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: "+ Activity.RESULT_OK);
        if (resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "onActivityResult: ");
            if (requestCode == RC_CAMERA_INTENT) {
                File f = new File(imageUtils.getCurrentPhotoPath());
                imageUtils.setImageUri(Uri.fromFile(f));
                fragmentAddMealsBinding.imgvMeal.setImageURI(imageUtils.getImageUri());
                Log.d(TAG, "ABsolute Url of Image is " + Uri.fromFile(f));
            }
            if (requestCode == RC_GALLERY) {
                imageUtils.setImageUri(data.getData());
                imageUtils.createImageFilenameWithExtention();
                 Log.d(TAG, "onActivityResult: Gallery Image Uri:  " + imageFileName);
                fragmentAddMealsBinding.imgvMeal.setImageURI(imageUtils.getImageUri());
            }
            if (requestCode == RC_CHOOSER_INTENT) {
                Log.i(TAG, "onActivityResult: chooserIntent" );
                if(data != null) {
                    imageUtils.setImageUri(data.getData());
                    imageUtils.createImageFilenameWithExtention();
                    Log.d(TAG, "onActivityResult: Gallery Image Uri:  " + imageUtils.getImageUri());
                }else {
                    File f = new File(imageUtils.getCurrentPhotoPath());
                    imageUtils.setImageUri(Uri.fromFile(f));
                    Log.d(TAG, "Absolute Url of Image is " + Uri.fromFile(f));
                }
                fragmentAddMealsBinding.imgvMeal.setImageURI(imageUtils.getImageUri());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMESIONS) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        AlertDialog show = new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.permission_camera_denied_rationale))
                                .setMessage(getString(R.string.permission_denied_message))
                                .setPositiveButton(getString(R.string.enable), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, RC_PERMISSIONSETTINGS);
                                    }
                                }).show();
                    } else if (Manifest.permission.CAMERA.equals(permission)) {
//                        showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                        Toast.makeText(getActivity(), getString(R.string.permission_camera_denied_rationale), Toast.LENGTH_LONG).show();

                    }
//                    else if ( /* possibly check more permissions...*/) {
//                    }
                } else {
                    imageUtils.dispatchTakePictureIntent();
                }

            }
        }

    }

    public void saveMeal(){
        String name = fragmentAddMealsBinding.etxtNameAddMealFragment.getText().toString();
        String cat = fragmentAddMealsBinding.spinnerCatSaveMeal.getSelectedItem().toString();
        String desc = fragmentAddMealsBinding.etxtDescriptionAddMeal.getText().toString();
        int cal = Integer.parseInt(fragmentAddMealsBinding.etxtCaloriesAddMealFragment.getText().toString());
        int price = Integer.parseInt(fragmentAddMealsBinding.etxtPriceAddMealFragmetn.getText().toString());
        final Meal meal = new Meal(name,cat,desc ,cal ,price);

        mViewModel.storeImage(imageUtils.getImageFileName(), imageUtils.getImageUri()).observe(getViewLifecycleOwner(),
                new Observer<String>() {
            @Override
            public void onChanged(String s) {
                meal.setImageUri(s);
                mViewModel.saveMeal(meal).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            Toast.makeText(getActivity(),"success",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

    }

}
