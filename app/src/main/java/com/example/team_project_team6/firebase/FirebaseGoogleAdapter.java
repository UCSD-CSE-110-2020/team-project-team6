package com.example.team_project_team6.firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.team_project_team6.model.Route;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

                    // Create the user document if it does not exist (first time user?)
                    // Adds the user to a random team by default
                    db.collection("users")
                            .document(getEmail())
                            .get()
                            .addOnCompleteListener(createTask -> {
                        if (createTask.isSuccessful()) {
                            DocumentSnapshot document = createTask.getResult();
                            if (!Objects.requireNonNull(document).exists()) {
                                Map<String, String> team = new HashMap<>();
                                String uuid = UUID.randomUUID().toString();
                                team.put("team", uuid);

                                Log.d(TAG, "No team found, assuming new user. Creating team " + uuid + " for user " + getEmail());

                                Log.d(TAG, "Creating user " + getEmail());
                                db.collection("users")
                                        .document(getEmail())
                                        .set(team);

                                Map<String, List<String>> uuidTeam = new HashMap<>();
                                List<String> members = new ArrayList<>();
                                members.add(getEmail());
                                uuidTeam.put(uuid, members);

                                Log.d(TAG, "Creating team " + uuid);
                                db.collection("teams")
                                        .document(uuid)
                                        .set(uuidTeam);
                            } else {
                                Log.d(TAG, "Found user " + getEmail());
                            }
                        }
                    });

                    //set token ID when user login or logout
                    Map<String, Object> token_id = new HashMap<>();
                    token_id.put("token_id", FirebaseInstanceId.getInstance().getToken());
                    db.collection("users").document(getEmail()).update(token_id)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Token ID successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating Token ID", e);
                                }
                            });

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
    public String getUsername(){
        if (user == null) {
            Log.e(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getDisplayName();
        }
    }


    @Override
    public void uploadRouteData(Route route) {
        if (user == null) {
            Log.d(TAG, "Could not upload route data without signing in");
            return;
        }

        Gson gson = new Gson();
        Map<String, String> jsonToMap = gson.fromJson(
                gson.toJson(route), new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        Log.d(TAG, "save with: " + getEmail());
        DocumentReference uidRef = db.collection("users").document(getEmail());
        uidRef.collection("routes")
                .document(route.getName())
                .set(jsonToMap)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Route saved to routes/" + route.getName() + "/");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error adding document", e);
                });
    }

    @Override
    public synchronized LiveData<ArrayList<Route>> downloadRouteData() {
        MutableLiveData<ArrayList<Route>> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not download route data without signing in");
            return data;
        }

        Gson gson = new Gson();
        db.collection("users")
                .document(getEmail())
                .collection("routes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "Successfully read data but data was empty");
                    } else {
                        ArrayList<Route> list = new ArrayList<>();

                        List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshots) {
                            Map<String, Object> map = snapshot.getData();
                            Route route = gson.fromJson(gson.toJson(map), Route.class);
                            list.add(route);
                        }

                        Log.i(TAG, "Successfully read route data");

                        data.postValue(list);
                    }
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));

        return data;
    }

    @Override
    public void updateNotification(String email){
        if (user == null) {
            Log.d(TAG, "Could not download route data without signing in");
            return;
        }

        String message = "You received an invitation from ";
        if(getUsername() != null) {
            message += getUsername();
        }else{
            message += email;
        }
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> inv = new HashMap<>();
        inv.put("from", getEmail());
        inv.put("message", message);
        data.put("invitation",  inv);


        db.collection("users").document(email).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Invitation successfully updated!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating Invitation", e);
                }
            });

    }

}