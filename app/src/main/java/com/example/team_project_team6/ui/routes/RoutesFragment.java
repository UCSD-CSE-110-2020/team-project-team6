package com.example.team_project_team6.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;

import java.util.ArrayList;
import java.util.Calendar;

public class RoutesFragment extends Fragment {

    private RoutesViewModel routesViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routesViewModel = ViewModelProviders.of(requireActivity()).get(RoutesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_routes, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_routes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final RouteViewAdapter adapter = new RouteViewAdapter();
        mAdapter = adapter;
        recyclerView.setAdapter(mAdapter);

        routesViewModel.getRouteData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Route>>() {
            @Override
            public void onChanged(ArrayList<Route> routes) {
                adapter.updateData(routes);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnFavoriteClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    Features feature = route.getFeatures();
                    feature.setFavorite(!feature.isFavorite());
                    route.setFeatures(feature);

                    routesViewModel.updateRouteAt(position, route);
                }
            }
        });

        adapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    Log.d("Routes", "Clicked on route: " + route.getName());
                }
            }
        });

        return root;
    }
}