package com.example.alfhana.ui.loginactivity;

import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.example.alfhana.R;
import com.example.alfhana.data.UserRepository;
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.SignUpFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;

    private static final String TAG = "SignUpFragment";
    public static final int RC_CAMERA_INTENT = 103;
    public static final int RC_PERMISSIONSETTINGS = 104;
    public static final int RC_GALLERY = 107;
    private Bitmap bitmap;
    private static final int RC_PERMESIONS = 101;
    public static final int RC_CHOOSER_INTENT = 102;
    private String currentPhotoPath;
    Uri userImageUri;
    private StorageReference mStorageRef;
    private SignUpViewModel mViewModel;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"};
    UserRepository userRepository;
    SignUpFragmentBinding signUpFragmentBinding;
    Uri mImageUri;
    private String imageFileName;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        signUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false);
        signUpFragmentBinding.setCamera(this);

        userRepository = UserRepository.getInstance(getActivity());

        database = FirebaseDatabase.getInstance();
        myRef  = database.getReference(getString(R.string.user_table));
        return signUpFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }

    //when the user clicks on the userImage
    public void askPermissions() {

        if (allPermissionsGranted()) {
            createChooserIntent();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, RC_PERMESIONS);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_CAMERA_INTENT) {
                File f = new File(currentPhotoPath);
                mImageUri = Uri.fromFile(f);
                signUpFragmentBinding.imgvUser.setImageURI(mImageUri);
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
            }
            if (requestCode == RC_GALLERY) {
                mImageUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFileName = "JPEG_" + timeStamp + "." + getFileExt(mImageUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                signUpFragmentBinding.imgvUser.setImageURI(mImageUri);
            }
            if (requestCode == RC_CHOOSER_INTENT) {
                if(data != null) {
                    mImageUri = data.getData();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    imageFileName = "JPEG_" + timeStamp + "." + getFileExt(mImageUri);
                    Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                }else {
                    File f = new File(currentPhotoPath);
                    mImageUri = Uri.fromFile(f);
                    Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                }
                signUpFragmentBinding.imgvUser.setImageURI(mImageUri);
            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
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
                    dispatchTakePictureIntent();
                }

            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
//        mImageUri = Uri.fromFile(image);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.alfhana.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, RC_CAMERA_INTENT);
            }
        }

    }

    private void createChooserIntent() {
        Intent gallIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(gallIntent, getString(R.string.user_image_chooser_title));
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.alfhana.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});
            }
        }
        startActivityForResult(chooserIntent, RC_CHOOSER_INTENT);
    }

    public void register() {
        LiveData<Boolean> isRegistered =
        userRepository.register(signUpFragmentBinding.etxtEmailSignup.getText().toString().trim(),
                signUpFragmentBinding.etxtPsswrd.getText().toString().trim());
        isRegistered.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    User user = createUser();
                    userRepository.saveUser(user);
                }
            }
        });
        userRepository.getLoggedInUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                SignUpFragmentDirections.ActionSignUpFragmentToMealsActivity action =
                        SignUpFragmentDirections.actionSignUpFragmentToMealsActivity();
                action.setLoggedinUser(user);
                Navigation.findNavController(getView()).navigate(action);
            }
        });
//
    }
    private User createUser() {
        String name = signUpFragmentBinding.etxtNameSignupActivity.getText().toString();
        String email = signUpFragmentBinding.etxtEmailSignup.getText().toString();
        String address = signUpFragmentBinding.etxtAddressSignup.getText().toString();
        String phone = signUpFragmentBinding.etxtPhoneSignup.getText().toString();
        User user = new User(name,email,phone,address);

        if(mImageUri != null) {
            String firebaseUri = userRepository.storeImage(imageFileName, mImageUri);
            user.setImage(firebaseUri);
        }

        return user;
    }

}
