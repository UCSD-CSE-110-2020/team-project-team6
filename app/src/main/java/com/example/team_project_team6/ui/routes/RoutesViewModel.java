package com.example.team_project_team6.ui.routes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class RoutesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;

    public RoutesViewModel() {
        ArrayList<Route> data = new ArrayList<Route>();
        data.add(new Route(new Walk(), "starting_point", null, "", new Features(), "Mission Bay"));
        data.add(new Route(new Walk(), "starting_point", Calendar.getInstance(), "", new Features(0, 0, 0, true, 0, 0), "Park Park"));

        mRoutes = new MutableLiveData<>(data);
    }

    LiveData<ArrayList<Route>> getRouteData() {
        return mRoutes;
    }

    Route getRouteAt(int index) {
        ArrayList<Route> data = mRoutes.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                return data.get(index);
            }
        }

        return null;
    }

    void updateRouteAt(int index, Route newRoute) {
        ArrayList<Route> data = mRoutes.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                data.set(index, newRoute);
                mRoutes.postValue(data);
            }
        }
    }
}