package com.example.team_project_team6.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.HashMap;

public interface IFirebase {
    void authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account);

    Boolean getSignedIn();
    String getEmail();
    String getId();
    LiveData<String> getTeamUUID();
    String getName();

    void uploadRouteData(Route route);
    LiveData<ArrayList<Route>> downloadRouteData(String email);

    void acceptTeamRequest();
    void declineTeamRequest();
    void uploadTeamRequest(String member);
    LiveData<HashMap<String, String>> downloadTeamRequest();
    LiveData<ArrayList<TeamMember>> downloadTeamData();

}
