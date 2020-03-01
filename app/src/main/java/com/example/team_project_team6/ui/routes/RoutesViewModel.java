package com.example.team_project_team6.ui.routes;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RoutesViewModel extends AndroidViewModel {
    ArrayList<Route> myRoutes, teamRoutes;
    private MutableLiveData<ArrayList<Route>> mRoutes;
    private Context context;
    private SaveData saveData;
    private boolean teamView;

    public RoutesViewModel(Application application) {
        super(application);
        ArrayList<Route> data = new ArrayList<Route>();
        mRoutes = new MutableLiveData<>(data);
        this.context = application;
        saveData = new SaveData(context);
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
            route.setInitials("TEST INITIALS");
            routeList.add(route);
        }
        mRoutes.postValue(routeList);
        setRouteData(mRoutes);
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
                saveData.saveRoute(newRoute);
                mRoutes.postValue(data);
            }
        }
    }

    public void setTeamView(boolean b) {
        teamView = b;
    }
}