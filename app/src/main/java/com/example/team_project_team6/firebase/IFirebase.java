package com.example.team_project_team6.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.team_project_team6.model.Route;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public interface IFirebase {
    void authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account);

    Boolean getSignedIn();
    String getEmail();
    String getId();

    void uploadRouteData(Route route);
    LiveData<ArrayList<Route>> retrieveRouteDoc();
    FirestoreRecyclerOptions<Route> fbaseRclOptRoute();
    void updateFavorite(String id, boolean isFavorite);
}
