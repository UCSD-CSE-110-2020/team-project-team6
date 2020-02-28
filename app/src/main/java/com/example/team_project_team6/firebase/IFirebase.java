package com.example.team_project_team6.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.team_project_team6.model.Route;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface IFirebase {
    boolean authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account);

    String getEmail();
    String getId();

    void uploadRouteData(Route route);
}
