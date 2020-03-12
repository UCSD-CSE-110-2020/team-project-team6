package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.team_project_team6.firebase.IFirebase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

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
        return firebaseAdapter.downloadRouteData(firebaseAdapter.getEmail());
    }

    public LiveData<ArrayList<Route>> getRoutesFor(String email) {
        return firebaseAdapter.downloadRouteData(email);
    }

    public LiveData<ArrayList<TeamMember>> getAllMembers() {
        return firebaseAdapter.downloadTeamData();
    }

    public LiveData<String> getTeam() {
        return firebaseAdapter.getTeamUUID();
    }

    public void addTeamMember(String email) {
        firebaseAdapter.uploadTeamRequest(email);
    }

    public void addProposedWalk(ProposedWalk proposedWalk) {
        firebaseAdapter.uploadProposedWalk(proposedWalk);
    }

    public LiveData<ProposedWalk> getProposedWalk() {
        return firebaseAdapter.downloadProposedWalk();
    }

    public LiveData<HashMap<String, String>> getTeamInviter() {
        return firebaseAdapter.downloadTeamRequest();
    }

    public LiveData<HashMap<String, String>> getMemberGoingStatuses() {
        return firebaseAdapter.downloadMemberGoingStatuses();
    }

    public void acceptTeamRequest() {
        firebaseAdapter.acceptTeamRequest();
    }

    public void declineTeamRequest() {
        firebaseAdapter.declineTeamRequest();
    }

    public void updateMemberGoingStatus(String attendance) {
        firebaseAdapter.uploadMemberGoingStatus(attendance);

    }

    public String getEmail() {
        return firebaseAdapter.getEmail();
    }

    public String getName() {
        return firebaseAdapter.getName();
    }

    public void sendTeamNotification(TeamMessage message, boolean isMessageForProposeWalk){
        firebaseAdapter.sendTeamNotification(message, isMessageForProposeWalk);
    }

    public void deleteProposedWalk() {
        firebaseAdapter.deleteProposedWalk();
    }
}
