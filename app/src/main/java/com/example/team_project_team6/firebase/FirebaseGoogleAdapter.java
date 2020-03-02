package com.example.team_project_team6.firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.model.Route;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseGoogleAdapter implements IFirebase {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String TIMESTAMP_KEY = "timestamp";

    public FirebaseGoogleAdapter() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = null;
    }

    @Override
    public void authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(authCredential).addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Signed in with Google to Firebase");
                    user = auth.getCurrentUser();

                    Toast.makeText(activity, "Signed into Firebase successfully", Toast.LENGTH_LONG).show();
                    Toast.makeText(activity, "Logged in with email: " + getEmail(), Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "Failed to sign in to Firebase");
                    Toast.makeText(activity, "ERROR: Failed to sign into Firebase", Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public Boolean getSignedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public String getEmail() {
        if (user == null) {
            Log.e(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getEmail();
        }
    }

    @Override
    public String getId() {
        if (user == null) {
            Log.e(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getUid();
        }
    }

    @Override
    public void uploadRouteData(Route route) {
        if (user == null) {
            Log.d(TAG, "Could not upload route data without signing in");
            return;
        }

        Gson gson = new Gson();
        Map<String, String> updates = new HashMap<>();

        //convert json to Map
        Map<String, Object> jsonToMap = gson.fromJson(
                gson.toJson(route), new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        //put timestamp to order by date
        jsonToMap.put(TIMESTAMP_KEY, FieldValue.serverTimestamp());

        Log.d(TAG, "save with: " + getEmail());
        DocumentReference uidRef = db.collection("users").document(getEmail());
        uidRef.collection("routes")
                .add(jsonToMap)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error adding document", e);
                });

    }

    @Override
    public LiveData<ArrayList<Route>> retrieveRouteDoc() {
        if (user == null) {
            Log.d(TAG, "Could not upload route data without signing in");
            return null;
        }

        CollectionReference uidRef = db.collection("users").document(getEmail()).collection("routes");

        return new FirestoreLiveData<>(uidRef, Route.class, TIMESTAMP_KEY);

    }

    @Override
    public FirestoreRecyclerOptions<Route> fbaseRclOptRoute(){
        CollectionReference uidRef = db.collection("users").document(getEmail()).collection("routes");
        Query query = uidRef.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Route> options = new FirestoreRecyclerOptions.Builder<Route>()
                .setQuery(query, new SnapshotParser<Route>() {
                    @NonNull
                    @Override
                    public Route parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Log.i("FirestoreRecyclerOptions", snapshot.getId());
                        Gson gson_route = new Gson();
                        Route tmp = gson_route.fromJson(gson_route.toJson(snapshot.getData()), Route.class);
                        Log.i("FirestoreRecyclerOptions", tmp.getName() + snapshot.getId());
                        return tmp;
                    }
                }).build();
        return options;
    }

    @Override
    public void updateFavorite(String id, boolean isFavorite){
        CollectionReference uidRef = db.collection("users").document(getEmail()).collection("routes");
        uidRef.document(id).
                update("features.isFavorite", isFavorite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Favorite successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating Favorite", e);
                    }
                });
    }

}