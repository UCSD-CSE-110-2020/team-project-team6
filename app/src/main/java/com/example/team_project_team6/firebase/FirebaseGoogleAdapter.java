package com.example.team_project_team6.firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.team_project_team6.model.Route;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

        Log.d(TAG, "save with: " + getEmail());
        DocumentReference uidRef = db.collection("users").document(getEmail());
        uidRef.collection("routes")
                .document(route.getName())
                .set(route)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Route saved to routes/" + route.getName() + "/");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error adding document", e);
                });

    }

    @Override
    public ArrayList<Route> downloadRouteData() {
        if (user == null) {
            Log.d(TAG, "Could not download route data without signing in");
            return new ArrayList<>();
        }

        AtomicReference<Boolean> complete = new AtomicReference<>(false);
        ArrayList<Route> routes = new ArrayList<>();
        db.collection("users")
                .document(getEmail())
                .collection("routes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "Successfully read data but data was empty");
                    } else {
                        List<Route> data = queryDocumentSnapshots.toObjects(Route.class);
                        routes.addAll(data);

                        Log.i(TAG, "Successfully read route data");
                    }

                    complete.set(true);
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));

        // Keep waiting every 100 ms to load data from firebase
        while (!complete.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return routes;
    }
}