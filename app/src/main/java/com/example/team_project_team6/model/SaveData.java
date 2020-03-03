package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.team_project_team6.firebase.IFirebase;
import com.google.gson.Gson;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SaveData {
    private Gson gson;
    private SharedPreferences spfsUser;
    private SharedPreferences spfsRoute;
    private IFirebase firebaseAdapter;

    public SaveData(Context mainActivity, IFirebase firebaseAdapter) {
        Log.i("Creating new SaveData", "new SaveData with context: " + mainActivity.toString());
        gson = new Gson();
        spfsUser = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        spfsRoute = mainActivity.getSharedPreferences("route_data", MODE_PRIVATE);
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

    public String saveRoute(Route route) {
        // convert walk into a json object
        String json = gson.toJson(route);

        // save the walk information into SharedPreferences to be retrieved when the Route is saved/updated
        SharedPreferences.Editor editor = spfsRoute.edit();
        editor.putString(route.getName(), json);
        editor.apply();

        Log.i("Saving Route " + route.getName() + " in SaveData", json);

        return json;
    }

    public Set<String> getRouteNames() {
        Log.i("Retrieving All Routes from SaveData", "Getting all Route names...");
        return spfsRoute.getAll().keySet();
    }

    public Route getRoute(String name) {
        String routeJson = spfsRoute.getString(name, "");
        Log.i("Retrieving Route from SaveData", routeJson);

        return gson.fromJson(routeJson, Route.class);
    }
}