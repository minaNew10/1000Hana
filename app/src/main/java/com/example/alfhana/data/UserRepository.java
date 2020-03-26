package com.example.alfhana.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.alfhana.R;
import com.example.alfhana.data.model.User;
import com.example.alfhana.ui.loginactivity.LoginFragmentDirections;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
    private static final String TAG = "UserRepository";
    private static volatile UserRepository instance;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("UsersPhotos");;

    private MutableLiveData<User> loggedInUser = new MutableLiveData<>();

    public MutableLiveData<Boolean> getRegisterationSuccessful() {
        return registerationSuccessful;
    }

    private MutableLiveData<Boolean> registerationSuccessful = new MutableLiveData<>();

    private MutableLiveData<Boolean> isAdmin = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsAdmin() {
        return isAdmin;
    }
    Context context;

    private UserRepository(Context context) {
        this.context = context;
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    public boolean CheckLoginState() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            return true;
        }
        return false;
    }

    private void retrieveUserFromDatabase(final String id) {
        databaseReference
                .child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Log.d(TAG, "onDataChange: "+dataSnapshot.getValue(User.class).toString());
                        if (dataSnapshot.hasChild(id)) {
                            User user = dataSnapshot.child(id).getValue(User.class);
                            Log.i(TAG, "onDataChange: " + user);
                            loggedInUser.postValue( dataSnapshot.child(id).getValue(User.class));
                        }else {
                            Log.i(TAG, "onDataChange: user is not avaialble");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void login(String email, String password) {
        // handle login
        firebaseAuth.signInWithEmailAndPassword(email,
                password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String id = firebaseAuth.getUid();
                            retrieveUserFromDatabase(id);
//                            isAdmin(id);
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+ e.getMessage());
            }
        });
    }

    private void isAdmin(String id) {
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
    }


    public LiveData<Boolean> register(String email, String psswrd) {
        firebaseAuth.createUserWithEmailAndPassword(email,
                psswrd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerationSuccessful.postValue(true);
                        } else {
                            Toast.makeText(context, context.getString(R.string.registeration_failed), Toast.LENGTH_LONG).show();
                            registerationSuccessful.postValue(false);
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        registerationSuccessful.postValue(false);
                        Toast.makeText(context, context.getString(R.string.registeration_failed) + " why ", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthWeakPasswordException) {
                    registerationSuccessful.postValue(false);
                    Toast.makeText(context, context.getString(R.string.registeration_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
        return registerationSuccessful;
    }



    public MutableLiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void storeImage(String imageFileName, Uri imageUri) {

        storageRef = FirebaseStorage.getInstance().getReference().child("UsersPhotos/" + imageFileName);
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                          @Override
                                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                              storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                  @Override
                                                  public void onSuccess(Uri uri) {

                                                  }
                                              });
                                          }
                                      }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public boolean saveUser(User user) {
        databaseReference.child(firebaseAuth.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, context.getString(R.string.registeration_successful), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.registeration_failed), Toast.LENGTH_LONG).show();

                        }

                    }
                });


        return true;
    }
}