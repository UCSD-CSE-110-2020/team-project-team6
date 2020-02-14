package com.example.team_project_team6.ui.routes;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class RoutesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;

    public RoutesViewModel() {
        ArrayList<Route> data = new ArrayList<Route>();
        mRoutes = new MutableLiveData<>(data);
    }

    // mRoutes stores a list of all the routes in sharedpreferences
    public void setRouteData(MutableLiveData<ArrayList<Route>> mRoutes) {
        this.mRoutes = mRoutes;
    }

    public LiveData<ArrayList<Route>> getRouteData() {
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