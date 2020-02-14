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
import java.util.Comparator;
import java.util.Objects;
import java.util.TimeZone;

public class RoutesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;

    public RoutesViewModel() {
        ArrayList<Route> data = new ArrayList<Route>();
        Walk w = new Walk();
        w.setDist(123.456);
        w.setStep(612);
        w.setDuration("00:24:11");
        w.setStartTime(Calendar.getInstance());
        Features f = new Features();
        f.setDirectionType(1);
        f.setLevel(1);
        f.setSurface(1);
        f.setTerrain(1);
        f.setType(1);
        data.add(new Route(w, "University of California, San Diego, EBU3B", null, "Test Walk Notes", f, "Mission Bay"));
        data.add(new Route(new Walk(), "starting_point", Calendar.getInstance(), "", new Features(0, 0, 0, true, 0, 0), "Aardvark Park"));

        mRoutes = new MutableLiveData<>(data);
    }

    public LiveData<ArrayList<Route>> getRouteData() {
        mRoutes.getValue().sort(new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

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