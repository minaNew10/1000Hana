package com.example.alfhana.data.repository;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.model.User;
import com.example.alfhana.utils.FirebaseQueryLiveData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class MealRepository {
    private static volatile MealRepository instance;
    private static final String TAG = "MealRepository";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
    private MutableLiveData<String> saveMealErr =new MutableLiveData<>();
    private MutableLiveData<String> saveMealPhotoErr =new MutableLiveData<>();

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("MealsPhotos/");;
    private MealRepository(){

    }

    public static MealRepository getInstance(){
        if(instance == null){
            instance = new MealRepository();
        }
        return instance;
    }
    public FirebaseQueryLiveData getMeals(@Meal.Category String category){
        Log.i(TAG, "getMeals: ");
        Query query = databaseReference
                .child("meals")
                .child(category);

        final FirebaseQueryLiveData mealsList  = new FirebaseQueryLiveData(query);
        return mealsList;
    }
    public void getMeals2(@Meal.Category String category){
        Log.i(TAG, "getMeals: ");
//        final MutableLiveData<List<Meal>> mealsList  = new MutableLiveData<>();
        databaseReference
                .child("meals")
                .child(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Log.i(TAG, "onDataChange: " + dataSnapshot1.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
//        return mealsList;
    }
    public MutableLiveData<Boolean> saveMeal(Meal meal) {
        final MutableLiveData<Boolean> savedSuccessfully = new MutableLiveData<>();
        databaseReference.child("meals")
                .child(meal.getCategory())
                .child(meal.getName())
                .setValue(meal)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            savedSuccessfully.postValue(true);
                        }else {
                            savedSuccessfully.postValue(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                saveMealErr.postValue(e.getMessage());
            }
        });
        return savedSuccessfully;

    }

    public MutableLiveData<String> getSaveMealErr() {
        return saveMealErr;
    }

    public MutableLiveData<String> storeImage(String imageFileName, Uri imageUri) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        storageRef = FirebaseStorage.getInstance().getReference().child("MealsPhotos/" + imageFileName);
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
                saveMealErr.postValue(e.getMessage());
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<String> getSaveMealPhotoErr() {
        return saveMealPhotoErr;
    }
}
