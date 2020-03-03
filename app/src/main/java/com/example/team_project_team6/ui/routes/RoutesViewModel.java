package com.example.team_project_team6.ui.routes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RoutesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;
    private SaveData saveData;

    public RoutesViewModel() {
        ArrayList<Route> data = new ArrayList<Route>();
        mRoutes = new MutableLiveData<>(data);
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public SaveData getSaveData(SaveData saveData) {
        return this.saveData;
    }

    // mRoutes stores a list of all the routes in firebase
    public void setRouteData(MutableLiveData<ArrayList<Route>> mRoutes) {
        this.mRoutes = mRoutes;
    }


    // Routes are displayed in the same order they are present in
    public LiveData<ArrayList<Route>> getRouteData() {
        updateRouteData();
        return mRoutes;
    }

    public void updateRouteData() {
        mRoutes.postValue(saveData.getAllRoutes());
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