package com.example.team_project_team6.ui.route_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;

public class RouteDetailsViewModel extends ViewModel {
    private Route mRoute;

    public RouteDetailsViewModel() {
        mRoute = new Route(new Walk(), "", null, "", new Features(), "");
    }

    Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route route) {
        this.mRoute = route;
    }
}