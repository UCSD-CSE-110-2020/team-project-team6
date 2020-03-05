package com.example.team_project_team6.ui.new_route;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;

public class NewRouteViewModel extends ViewModel {
    private SaveData saveData;

    public NewRouteViewModel() { }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public SaveData getSaveData() {
        return this.saveData;
    }

    public void saveRoute(Route route) {
        if (saveData != null) {
            this.saveData.saveRoute(route);
        } else {
            Log.w("SaveRoute", "Warning save route is null in NewRouteViewModel");
        }
    }

    public Walk getWalk() {
        return saveData != null ? saveData.getWalk() : null;
    }
}
