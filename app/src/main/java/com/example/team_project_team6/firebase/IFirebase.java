package com.example.team_project_team6.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.team_project_team6.model.Route;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

public interface IFirebase {
    void authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account);

    Boolean getSignedIn();
    String getEmail();
    String getId();
    String getUsername();


    void uploadRouteData(Route route);
    LiveData<ArrayList<Route>> downloadRouteData();

    void updateNotification(String email);
}