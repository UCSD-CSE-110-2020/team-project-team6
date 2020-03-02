package com.example.team_project_team6.firebase;

import android.util.Log;

import com.example.team_project_team6.model.Route;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirestoreLiveData<T> extends LiveData<T> {

    private CollectionReference uidRef;
    private ListenerRegistration registration;
    private Class myClass;
    private String TIMESTAMP_KEY;

    public FirestoreLiveData(CollectionReference uidRef, Class myClass, String timeStampKey){
        this.uidRef = uidRef;
        this.myClass = myClass;
        this.TIMESTAMP_KEY = timeStampKey;
    }

    EventListener<QuerySnapshot> eventListener = (queryDocumentSnapshots, e) -> {
        if (e != null) {
            Log.i(TAG, "Listen failed.", e);
            return;
        }

        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
            Gson gson_route = new Gson();
            ArrayList<T> routeList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                Log.d(TAG, "Route ID: " + document.getId() + " => " + document.getData());
                T tmp = (T) gson_route.fromJson(gson_route.toJson(document.getData()), myClass);
                //Log.d(TAG, "Route class: " + tmp.getName() + " => " + tmp.getStartPoint());
                routeList.add(tmp);
            }
            setValue((T) routeList);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        registration = uidRef.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING).addSnapshotListener(eventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (!hasActiveObservers()) {
            registration.remove();
            registration = null;
        }
    }

}
