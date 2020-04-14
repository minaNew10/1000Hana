package com.example.alfhana.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.alfhana.data.model.Meal;
import com.example.alfhana.data.model.Request;
import com.example.alfhana.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestRepository {
    private static volatile RequestRepository instance;
    private static final String TAG = "MealRepository";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
    private MutableLiveData<String> saveRequestErr =new MutableLiveData<>();
    private RequestRepository(){

    }

    public static RequestRepository getInstance(){
        if(instance == null){
            instance = new RequestRepository();
        }
        return instance;
    }

    public MutableLiveData<Boolean> placeOrder(Request request) {
        User user  = UserRepository.getInstance().getUser();
        final MutableLiveData<Boolean> savedSuccessfully = new MutableLiveData<>();
        databaseReference.child("requests")
                .child(UserRepository.getInstance().getFirebaseUser().getUid())
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(request)
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
                Log.i(TAG, "onFailure: "+ e);
                saveRequestErr.postValue(e.getMessage());
            }
        });
        return savedSuccessfully;

    }
}
