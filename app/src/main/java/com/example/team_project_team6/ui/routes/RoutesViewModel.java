package com.example.team_project_team6.ui.routes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.firebase.IFirebase;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

public class RoutesViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;
    private Context context;
    private SaveData saveData;
    private IFirebase adapter;

    public RoutesViewModel(Application application) {
        super(application);
        ArrayList<Route> data = new ArrayList<Route>();
        mRoutes = new MutableLiveData<>(data);
        this.context = application;
        saveData = new SaveData(context);

    }
    public void setAdapter(IFirebase adapter) {
        this.adapter = adapter;
    }
    public IFirebase getAdapter() {
        return adapter;
    }

    // mRoutes stores a list of all the routes in sharedpreferences
    public void setRouteData(MutableLiveData<ArrayList<Route>> mRoutes) {
        this.mRoutes = mRoutes;
    }


    // Routes are displayed in the same order they are present in
    public LiveData<ArrayList<Route>> getRouteData() {
        // populate list of routes tiles
        MutableLiveData<ArrayList<Route>> mRoutes = new MutableLiveData<ArrayList<Route>>();
        Set<String> routeNameSet = saveData.getRouteNames();
        List<String> routeNameList = new ArrayList<>(routeNameSet);
        Collections.sort(routeNameList);

        ArrayList<Route> routeList = new ArrayList<>();
        for(String routeName : routeNameList) {
            Route route = saveData.getRoute(routeName);
            routeList.add(route);
        }

        mRoutes.postValue(routeList);
        setRouteData(mRoutes);

        //return a livedata from FirestoreLivedata class
        return adapter.retrieveRouteDoc();
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
                saveData.saveRoute(newRoute);
                mRoutes.postValue(data);
            }
        }
    }
}