package com.example.alfhana.data.repository;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.alfhana.data.model.Meal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MealRepository {
    private static volatile MealRepository instance;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
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
