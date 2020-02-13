package com.example.team_project_team6;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team_project_team6.model.Walk;
import com.google.gson.Gson;

public class SaveDataActivity extends AppCompatActivity {
    Walk walk;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_walk);
    }

    public void startNewWalk(){
        walk = new Walk();
    }

    public void stopWalk(String duration, long numSteps, double distance)
    {
        walk.setDist(distance);
        walk.setDuration(duration);
        walk.setStep(numSteps);

//        Gson gson = new Gson();
//        String json = gson.toJson(walk);
//
//        SharedPreferences spfs = getSharedPreferences("user_data", MODE_PRIVATE);
//        SharedPreferences.Editor editor = spfs.edit();
    }

    public Walk getWalk() {
        return walk;
    }
}
