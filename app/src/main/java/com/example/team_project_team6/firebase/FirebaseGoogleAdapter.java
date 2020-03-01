package com.example.team_project_team6.firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.model.Route;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseGoogleAdapter implements IFirebase {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;

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
                    Log.d("TAG", "Signed in with Google to Firebase");
                    user = auth.getCurrentUser();

                    Toast.makeText(activity, "Signed into Firebase successfully", Toast.LENGTH_LONG).show();
                    Toast.makeText(activity, "Logged in with email: " + getEmail(), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Failed to sign in to Firebase");
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
            Log.d(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getEmail();
        }
    }

    @Override
    public String getId() {
        if (user == null) {
            Log.d(TAG, "You must sign in before calling this method");
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

        //convert json to Map
        Map<String, Object> jsonToMap = gson.fromJson(
                gson.toJson(route), new TypeToken<HashMap<String, Object>>() {}.getType()
        );

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
}