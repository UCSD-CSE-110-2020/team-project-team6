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

    public int getHeight(){
        SharedPreferences spfs = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        return spfs.getInt("user_height", -1);
    }


    public void saveWalk(Walk walk){

        Gson gson = new Gson();
        String json = gson.toJson(walk);

        SharedPreferences spfs = mainActivity.getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.putString("walk", json);
        editor.apply();

        Log.i("saving walk", json);
    }
}
