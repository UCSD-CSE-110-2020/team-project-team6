package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.team_project_team6.firebase.IFirebase;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SaveData {
    private Gson gson;
    private SharedPreferences spfsUser;
    private IFirebase firebaseAdapter;

    public SaveData(Context mainActivity, IFirebase firebaseAdapter) {
        Log.i("Creating new SaveData", "new SaveData with context: " + mainActivity.toString());
        gson = new Gson();
        spfsUser = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        this.firebaseAdapter = firebaseAdapter;
    }

    public int getHeight() {
        int height = spfsUser.getInt("user_height", -1);
        Log.i("Retrieving User Height from SaveData", "Height: " + height);

        return height;
    }

    public String saveWalk(Walk walk) {
        // convert walk into a json object
        String json = gson.toJson(walk);

        // save the walk information into SharedPreferences to be retrieved when the Route is saved/updated
        SharedPreferences.Editor editor = spfsUser.edit();
        editor.putString("walk", json);
        editor.apply();

        Log.i("Saving Walk in SaveData", json);

        return json;
    }

    public Walk getWalk() {
        String walkJson = spfsUser.getString("walk", "");
        Log.i("Retrieving Walk from SaveData", walkJson);

        return gson.fromJson(walkJson, Walk.class);
    }

    public void saveRoute(Route route) {
        // convert walk into a json object
        String json = gson.toJson(route);

        firebaseAdapter.uploadRouteData(route);
        Log.i("Saving Route " + route.getName() + " in SaveData", json);
    }

    public LiveData<ArrayList<Route>> getAllRoutes() {
        return firebaseAdapter.downloadRouteData();
    }

    public void addTeamMember(TeamMember member) {
        firebaseAdapter.uploadTeamRequest(member);
    }

    public LiveData<ArrayList<TeamMember>> getAllMembers() {
        return firebaseAdapter.downloadTeamData();
    }

    public String getTeam() {
        String team = spfsUser.getString("team", "");
        Log.i(TAG, "Getting user's team from SharedPreferences");

        return team;
    }

    public void saveTeamMember(TeamMember teamMember) {
        // TODO save team member onto firebase
    }

    public void setTeam() {
        String team = firebaseAdapter.getTeam();

        SharedPreferences.Editor editor = spfsUser.edit();
        editor.putString("team", team);
        editor.apply();

        Log.i(TAG, "Saved user's team to SharedPreferences");
    }

    public String getEmail() {
        return firebaseAdapter.getEmail();
    }

}