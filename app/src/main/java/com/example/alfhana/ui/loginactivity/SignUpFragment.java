package com.example.alfhana.ui.loginactivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfhana.R;
import com.example.alfhana.databinding.SignUpFragmentBinding;

public class SignUpFragment extends Fragment {
    private Uri outputFileUri;
    private static final String TAG = "MainActivityy";

    private static final int RC_PERMESIONS = 101;
    public static final int RC_CHOOSER_INTENT = 102;
    private int PC_STORAGE = 103;

    private SignUpViewModel mViewModel;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"};

    SignUpFragmentBinding signUpFragmentBinding;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        signUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false);
        signUpFragmentBinding.setCamera(this);
        return signUpFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }

    public void askPermissions() {
        if (allPermissionsGranted()) {
            //start camera if permission has been granted by user
            createChooserIntent();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, RC_PERMESIONS);
            if(allPermissionsGranted()){
                createChooserIntent();
            }
        }
    }
    public boolean allPermissionsGranted() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void createChooserIntent() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent gallIntent = new Intent(Intent.ACTION_PICK);
        gallIntent.setType("image/*");
        final Intent chooserIntent = Intent.createChooser(gallIntent, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{camIntent});
        startActivityForResult(chooserIntent, RC_CHOOSER_INTENT);
}



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_CHOOSER_INTENT) {
            if (data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                signUpFragmentBinding.imgvUser.setImageDrawable(bitmapDrawable);
            } else if (data != null) {
                Uri uri = data.getData();

                signUpFragmentBinding.imgvUser.setImageURI(uri);
            }
        }
    }

}
