package com.example.team_project_team6.model;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.team_project_team6.MainActivity;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SaveData {
    MainActivity mainActivity;

    public SaveData(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    /**
     * retrieve the user's height from SharedPreferences
     * @return the user's height
     */
    public int getHeight() {
        SharedPreferences spfs = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        return spfs.getInt("user_height", -1);
    }


    public void saveWalk(Walk walk) {
        // convert walk into a json object
        Gson gson = new Gson();
        String json = gson.toJson(walk);

        // save the walk information into SharedPreferences to be retrieved when the Route is saved/updated
        SharedPreferences spfs = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.putString("walk", json);
        editor.apply();

        Log.i("Saving Walk in SaveData", json);
    }
}
