package com.example.alfhana.data.repository;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.alfhana.data.model.Request;
import com.example.alfhana.data.model.User;
import com.example.alfhana.database.AppDatabase;
import com.example.alfhana.utils.AppExecutors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository {
    private static volatile RequestRepository instance;

    List<Request> list = new ArrayList<>();
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

    public MutableLiveData<Boolean> placeRequestToServer(Request request) {
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

                saveRequestErr.postValue(e.getMessage());
            }
        });
        return savedSuccessfully;

    }

    public LiveData<Long> saveRequestToDatabase(Context context, final Request request){
        final AppDatabase appDatabase = AppDatabase.getInstance(context);
        final MutableLiveData<Long> idLive = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
               long id = appDatabase.requestDao().insert(request);
               idLive.postValue(id);
            }
        });
        return idLive;
    }

    public LiveData<List<Request>> getRequests(Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return appDatabase.requestDao().getAllRequestsLiveData();
    }
    public List<Request> getRequestsList(Context context){
        final AppDatabase appDatabase = AppDatabase.getInstance(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                list = appDatabase.requestDao().getAllrequestsInList();
            }
        });
        return list;
    }


}
