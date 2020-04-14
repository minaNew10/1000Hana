package com.example.alfhana.data.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.alfhana.R;
import com.example.alfhana.data.model.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UserRepository {
    private static final String TAG = "login";
    private static volatile UserRepository instance;
    private User user;


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("UsersPhotos/");



    private UserRepository() {

    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public MutableLiveData<Boolean> login(String email, String password) {
       final MutableLiveData<Boolean> isLoginSuccessful = new MutableLiveData<>();
        // handle login
        firebaseAuth.signInWithEmailAndPassword(email,
                password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG, "onComplete: repo" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            isLoginSuccessful.setValue(true);
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i(TAG, "onCanceled: repo");
                        isLoginSuccessful.setValue(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: repo" + e.getMessage());
                isLoginSuccessful.setValue(false);
            }
        });
        Log.i(TAG, "login: ");
        return isLoginSuccessful;
    }

    public FirebaseUser getFirebaseUser(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }
    public MutableLiveData<User> retrieveUserFromDatabase(final String id) {
        final MutableLiveData<User> loggedInUser = new MutableLiveData<>();
        databaseReference
                .child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Log.d(TAG, "onDataChange: "+dataSnapshot.getValue(User.class).toString());
                        if (dataSnapshot.hasChild(id)) {
                            user = dataSnapshot.child(id).getValue(User.class);
                            Log.i(TAG, "onDataChange: " + user);
                            loggedInUser.postValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(TAG, "onCancelled: ");
                            loggedInUser.postValue(null);
                    }
                });
        return loggedInUser;
    }
    public MutableLiveData<Boolean> isAdmin(String id) {
        final MutableLiveData<Boolean> isAdmin = new MutableLiveData<>();
        databaseReference.child("admins").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    isAdmin.postValue(true);
                } else {
                    isAdmin.postValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return isAdmin;
    }
    public MutableLiveData<Boolean> register(String email, String psswrd) {
        final MutableLiveData<Boolean> registerationSuccessful = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email,
                psswrd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerationSuccessful.postValue(true);
                        } else {
                            registerationSuccessful.postValue(false);
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        registerationSuccessful.postValue(false);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthWeakPasswordException) {
                    registerationSuccessful.postValue(false);

                }
            }
        });
        return registerationSuccessful;
    }
    public MutableLiveData<String> storeImage(String imageFileName, final Uri imageUri) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        storageRef = FirebaseStorage.getInstance().getReference().child("UsersPhotos/" + imageFileName);
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                          @Override
                                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                              storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                  @Override
                                                  public void onSuccess(Uri uri) {
                                                      mutableLiveData.postValue(uri.toString());
                                                  }
                                              });
                                          }
                                      }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+ e.getMessage());
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Boolean> saveUserInDatabase(final User user) {
        final MutableLiveData<Boolean> savedSuccessfully = new MutableLiveData<>();
        databaseReference.child("users")
                .child(firebaseAuth.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                          savedSuccessfully.postValue(true);
                        } else {
                            savedSuccessfully.setValue(false);

                        }

                    }
                });


        return savedSuccessfully;
    }

    public void logOut(){
        firebaseAuth.signOut();
    }
}