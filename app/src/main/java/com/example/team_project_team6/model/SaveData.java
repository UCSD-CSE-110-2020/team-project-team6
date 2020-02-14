package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team_project_team6.MainActivity;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SaveData {
    private Context mainActivity;

    public SaveData(Context mainActivity){
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


    public String saveWalk(Walk walk) {
        // convert walk into a json object
        Gson gson = new Gson();
        String json = gson.toJson(walk);

        // save the walk information into SharedPreferences to be retrieved when the Route is saved/updated
        SharedPreferences spfs = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.putString("walk", json);
        editor.apply();

        Log.i("Saving Walk in SaveData", json);

        return json;
    }
}
