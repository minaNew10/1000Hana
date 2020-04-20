package com.example.alfhana.ui.loginactivity.signup;

import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.example.alfhana.R;
import com.example.alfhana.data.model.LocationHelper;
import com.example.alfhana.data.model.User;
import com.example.alfhana.databinding.SignUpFragmentBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpFragment extends Fragment {

    public static final int RC_CAMERA_INTENT = 103;
    public static final int RC_PERMISSIONSETTINGS = 104;
    public static final int RC_GALLERY = 107;
    private static final int RC_PERMESIONS = 101;
    public static final int RC_CHOOSER_INTENT = 102;
    private SignUpViewModel mSignUpViewModel;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
    MutableLiveData<Boolean> mRegisterationSuccessfulMutableLivedata;
    SignUpFragmentBinding mSignUpFragmentBinding;
    private String mCurrentPhotoPath;
    private MutableLiveData<User> mUserInput;
    private MutableLiveData<String> psswrd;

    Uri mImageUri;
    private String mImageFileName;
    MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();
    LocationHelper mLocationHelper;
    Toast register;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mSignUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false);
        mSignUpFragmentBinding.setSignup(this);
        Bundle b = getArguments();
        if(b != null){
            mLocationHelper =   b.getParcelable(getString(R.string.key_location));
            if(mLocationHelper != null) {
                mSignUpFragmentBinding.txtvLocationSaved.setVisibility(View.VISIBLE);
                mSignUpFragmentBinding.txtvLocationSaved.setText(R.string.location_saved);
            }
        }
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mSignUpViewModel.signUpDataChanged(mSignUpFragmentBinding.etxtNameSignupActivity.getText().toString().trim(),
                        mSignUpFragmentBinding.etxtPsswrd.getText().toString().trim(),mSignUpFragmentBinding.etxtEmailSignup.getText().toString().trim());
            }
        };
        mSignUpFragmentBinding.etxtPsswrd.addTextChangedListener(afterTextChangedListener);
        mSignUpFragmentBinding.etxtEmailSignup.addTextChangedListener(afterTextChangedListener);
        mSignUpFragmentBinding.etxtNameSignupActivity.addTextChangedListener(afterTextChangedListener);
        return mSignUpFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSignUpViewModel = ViewModelProviders.of(getActivity()).get(SignUpViewModel.class);
        mUserMutableLiveData.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                SignUpFragmentDirections.ActionSignUpFragmentToMealsActivity action =
                        SignUpFragmentDirections.actionSignUpFragmentToMealsActivity();
                action.setLoggedinUser(user);
                Navigation.findNavController(getView()).navigate(action);
            }
        });

        mSignUpViewModel.getSignUpFormState().observe(getViewLifecycleOwner(), new Observer<SignUpFormState>() {
            @Override
            public void onChanged(SignUpFormState signUpFormState) {

                if(signUpFormState == null){

                    return;
                }
                if(signUpFormState.isDataValid()){

                    mSignUpFragmentBinding.btnCreateAccount.setEnabled(true);
                    mSignUpFragmentBinding.btnCreateAccount.setAlpha(1);
                    mSignUpFragmentBinding.btnCreateAccount.setElevation(4f);
                }
                if(signUpFormState.getUsernameError() != null){

                    mSignUpFragmentBinding.btnCreateAccount.setEnabled(false);
                    mSignUpFragmentBinding.btnCreateAccount.setAlpha(0.5f);
                    mSignUpFragmentBinding.etxtNameSignupActivity.setError(getString(signUpFormState.getUsernameError()));
                }
                if(signUpFormState.getEmailError() != null){

                    mSignUpFragmentBinding.btnCreateAccount.setEnabled(false);
                    mSignUpFragmentBinding.btnCreateAccount.setAlpha(0.5f);
                    mSignUpFragmentBinding.etxtEmailSignup.setError(getString(signUpFormState.getEmailError()));
                }
                if(signUpFormState.getPasswordError() != null){

                    mSignUpFragmentBinding.btnCreateAccount.setEnabled(false);
                    mSignUpFragmentBinding.btnCreateAccount.setAlpha(0.5f);
                    mSignUpFragmentBinding.etxtPsswrd.setError(getString(signUpFormState.getPasswordError()));
                }
            }
        });

        Uri imageUri = mSignUpViewModel.getUserImageUri();
        if(imageUri != null){
            mSignUpFragmentBinding.imgvUser.setImageURI(imageUri);
        }

        mUserInput = mSignUpViewModel.getUserInput();
        mUserInput.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String name = user.getDisplayName();
                if(!name.isEmpty())
                    mSignUpFragmentBinding.etxtNameSignupActivity.setText(name);
                String email = user.getEmail();
                if(!email.isEmpty())
                    mSignUpFragmentBinding.etxtEmailSignup.setText(email);
                String address = user.getAddress();
                if(!address.isEmpty())
                    mSignUpFragmentBinding.etxtAddressSignup.setText(address);
                String phone = user.getPhone();
                if(!phone.isEmpty())
                    mSignUpFragmentBinding.etxtPhoneSignup.setText(phone);
            }
        });
        psswrd = mSignUpViewModel.getPsswrd();
        psswrd.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty())
                    mSignUpFragmentBinding.etxtPsswrd.setText(s);
            }
        });
        mImageUri = mSignUpViewModel.getUserImageUri();
        mImageFileName = mSignUpViewModel.getImageFileName();
    }

    @Override
    public void onPause() {
        super.onPause();
        User user = createUser();

        mSignUpViewModel.setUserInput(user);
        mSignUpViewModel.setPsswrd(mSignUpFragmentBinding.etxtPsswrd.getText().toString());
    }

    //when the user clicks on the userImage
    public void askPermissions() {

        if (allPermissionsGranted()) {
            createChooserIntent();
        } else {
            requestPermissions( REQUIRED_PERMISSIONS, RC_PERMESIONS);
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
            if (requestCode == RC_CHOOSER_INTENT) {
                if(data != null) {
                    mImageUri = data.getData();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    mImageFileName = "JPEG_" + timeStamp + "." + getFileExt(mImageUri);

                }else {
                    File f = new File(mCurrentPhotoPath);
                    mImageUri = Uri.fromFile(f);

                }

                mSignUpViewModel.setImageUri(mImageUri);
                mSignUpViewModel.setImageFileName(mImageFileName);
                mSignUpFragmentBinding.imgvUser.setImageURI(mSignUpViewModel.getUserImageUri());
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


                if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
//                    // user rejected the permission
//                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
//                    if (!showRationale) {
//                        // user also CHECKED "never ask again"
//                        // you can either enable some fall back,
//                        // disable features of your app
//                        // or open another dialog explaining
//                        // again the permission and directing to
//                        // the app setting
//                        AlertDialog show = new AlertDialog.Builder(getActivity())
//                                .setTitle(getString(R.string.permission_camera_denied_rationale))
//                                .setMessage(getString(R.string.permission_denied_message))
//                                .setPositiveButton(getString(R.string.enable), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
//                                        intent.setData(uri);
//                                        startActivityForResult(intent, RC_PERMISSIONSETTINGS);
//                                    }
//                                }).show();
//                    } else if (Manifest.permission.CAMERA.equals(permission)) {
////                        showRationale(permission, R.string.permission_denied_contacts);
//                        // user did NOT check "never ask again"
//                        // this is a good place to explain the user
//                        // why you need the permission and ask if he wants
//                        // to accept it (the rationale)
//                        Toast.makeText(getActivity(), getString(R.string.permission_camera_denied_rationale), Toast.LENGTH_LONG).show();
//
//                    }
////                    else if ( /* possibly check more permissions...*/) {
////                    }
                }
                else {
                    createChooserIntent();
                }

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                mImageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
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
    public void register(){

        mSignUpFragmentBinding.btnCreateAccount.setEnabled(false);
        mSignUpFragmentBinding.progressBarSignup.setVisibility(View.VISIBLE);
        register = Toast.makeText(getActivity(),getResources().getText(R.string.registering),Toast.LENGTH_SHORT);
        register.show();
        String email = mSignUpFragmentBinding.etxtEmailSignup.getText().toString().trim();
        String psswrd = mSignUpFragmentBinding.etxtPsswrd.getText().toString().trim();
        mRegisterationSuccessfulMutableLivedata = mSignUpViewModel.register(email,
                psswrd);
        mRegisterationSuccessfulMutableLivedata.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                register.cancel();
                if(aBoolean){
                    Log.i("uri", "onChanged: " + mImageUri);
                    final User user = createUser();
                    if (mImageUri != null) {

                        mSignUpViewModel.storeImage(mImageFileName, mImageUri).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                user.setImage(s);
                                mSignUpViewModel.saveUser(user);
                                mSignUpFragmentBinding.progressBarSignup.setVisibility(View.INVISIBLE);
                                register = Toast.makeText(getActivity(), getResources().getString(R.string.registeration_successful), Toast.LENGTH_LONG);
                                register.show();
//                             SignUpFragmentDirections.ActionSignUpFragmentToMealsActivity action =
//                                     SignUpFragmentDirections.actionSignUpFragmentToMealsActivity();
//                             action.setLoggedinUser(user);
//                             Navigation.findNavController(getView()).navigate(action);
                                Bundle b = new Bundle();
                                b.putParcelable(getString(R.string.user_key), user);
                                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build();
                                Navigation.findNavController(getView()).navigate(R.id.mealsActivity, b, navOptions);

                            }
                        });
                    }else{
                        mSignUpViewModel.saveUser(user);
                        mSignUpFragmentBinding.progressBarSignup.setVisibility(View.INVISIBLE);
                        register = Toast.makeText(getActivity(), getResources().getString(R.string.registeration_successful), Toast.LENGTH_LONG);
                        register.show();
//                             SignUpFragmentDirections.ActionSignUpFragmentToMealsActivity action =
//                                     SignUpFragmentDirections.actionSignUpFragmentToMealsActivity();
//                             action.setLoggedinUser(user);
//                             Navigation.findNavController(getView()).navigate(action);
                        Bundle b = new Bundle();
                        b.putParcelable(getString(R.string.user_key), user);
                        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build();
                        Navigation.findNavController(getView()).navigate(R.id.mealsActivity, b, navOptions);
                    }
                }else {
                    mSignUpFragmentBinding.progressBarSignup.setVisibility(View.INVISIBLE);
                    mSignUpViewModel.getErrRegisterMsg().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            register = Toast.makeText(getActivity(),s,Toast.LENGTH_LONG);
                            register.show();
                        }
                    });
                }
            }
        });
    }
    public void goToMaps(){
        NavHostFragment.findNavController(this).navigate(R.id.action_signUpFragment_to_mapFragment);
    }

    private User createUser() {
        String name = mSignUpFragmentBinding.etxtNameSignupActivity.getText().toString();
        String email = mSignUpFragmentBinding.etxtEmailSignup.getText().toString();
        String address = mSignUpFragmentBinding.etxtAddressSignup.getText().toString();
        String phone = mSignUpFragmentBinding.etxtPhoneSignup.getText().toString();
        User user = new User(name,email,phone,address, mLocationHelper);
        return user;
    }

}
