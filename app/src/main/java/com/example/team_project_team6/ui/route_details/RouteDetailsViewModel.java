package com.example.team_project_team6.ui.route_details;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;

public class RouteDetailsViewModel extends ViewModel {
    private Route mRoute;
    private boolean fromRouteDetails;

    public RouteDetailsViewModel() {
//        mRoute = new Route(new Walk(), "", null, "", new Features(), "");
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
