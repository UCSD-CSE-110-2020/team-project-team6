package com.example.team_project_team6.ui.new_route;

import com.example.team_project_team6.firebase.IFirebase;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;

import androidx.lifecycle.ViewModel;

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
        this.saveData.saveRoute(route);
    }

    public Walk getWalk() {
        return saveData.getWalk();
    }
}
