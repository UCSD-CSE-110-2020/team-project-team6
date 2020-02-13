package com.example.team_project_team6;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;
import com.google.gson.Gson;

import java.util.Calendar;

public class SaveDataActivity extends AppCompatActivity {
    Walk walk;
    Route route;
    Features features;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_walk);
    }

    public void createNewFeatures(int level, int directionType, int terrain, boolean isFavorite, int type, int surface) {
        features.setLevel(level);
        features.setDirectionType(directionType);
        features.setTerrain(terrain);
        features.setFavorite(isFavorite);
        features.setType(type);
        features.setSurface(surface);
    }

    public void updateRoute() {
        route.setWalk(walk);
    }

    public void createNewRoute(String startPoint, Calendar lastStartDate, String notes, String name){
        route.setFeatures(features);
        route.setWalk(walk);
        route.setLastStartDate(lastStartDate);
        route.setStartPoint(startPoint);
        route.setName(name);
    }

    public void startNewWalk(){
        walk = new Walk();
    }

    public void stopWalk(String duration, long numSteps, double distance)
    {
        walk.setDist(distance);
        walk.setDuration(duration);
        walk.setStep(numSteps);
    }


    public void saveInfo(){
        Gson gson = new Gson();
        String json = gson.toJson(route);

        SharedPreferences spfs = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.putString(route.getName(), json);
        editor.apply();
    }

    public Walk getWalk() {
        return walk;
    }

    public void setRoute(Route route){
        this.route = route;
    }
}
