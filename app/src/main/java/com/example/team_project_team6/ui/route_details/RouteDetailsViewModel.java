package com.example.team_project_team6.ui.route_details;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;

public class RouteDetailsViewModel extends ViewModel {
    private Route mRoute;
    private boolean fromRouteDetails;

    public SaveData getSaveData() {
        return saveData;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    private SaveData saveData;

    public RouteDetailsViewModel() {
        fromRouteDetails = false;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route currRoute) {
        if (currRoute != null) {
            Log.i("setCurrentRoute from RouteDetailsViewModel", "setting current route: " + currRoute.toString());
        } else {
            Log.i("setCurrentRoute from RouteDetailsViewModel", "setting current route: null");
        }
        this.mRoute = currRoute;
    }

    public boolean getIsWalkFromRouteDetails() {
        return this.fromRouteDetails;
    }

    public void setIsWalkFromRouteDetails(boolean isWalkFromRouteDetails) {
        this.fromRouteDetails = isWalkFromRouteDetails;
    }
}
