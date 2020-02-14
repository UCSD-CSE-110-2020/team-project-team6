package com.example.team_project_team6.model;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.MainActivity;
import com.google.gson.Gson;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SaveDataTest {

    private SaveData saveData;
    private Intent intent;

    @Before
    public void setUp(){
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
//        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testGetHeight() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            saveData = new SaveData(activity);

            SharedPreferences spfs = activity.getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor= spfs.edit();
            editor.putInt("user_height", 65);
            editor.apply();

            assertEquals(saveData.getHeight(), 65);
        });
    }

    @Test
    public void testSaveWalk() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Walk walk = new Walk();
            walk.setDist(20.2);
            walk.setStep(55);
            walk.setDuration("10:45:37");

            saveData = new SaveData(activity);
            saveData.saveWalk(walk);

            SharedPreferences spfs = activity.getSharedPreferences("user_data", MODE_PRIVATE);
            Gson gson = new Gson();
            String walkJson = gson.toJson(walk);

            assertEquals(spfs.getInt("walk", -1), walkJson);
        });
    }
}