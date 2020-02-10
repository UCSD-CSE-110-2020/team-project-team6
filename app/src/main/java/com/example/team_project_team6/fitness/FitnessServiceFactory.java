package com.example.team_project_team6.fitness;

import android.util.Log;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.StepCountActivity;
import com.example.team_project_team6.ui.home.HomeViewModel;
import com.example.team_project_team6.ui.walk.WalkFragment;

import java.util.HashMap;
import java.util.Map;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, MainActivity walk) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(walk);
    }

    public interface BluePrint {
        FitnessService create(MainActivity walk);
    }
}
