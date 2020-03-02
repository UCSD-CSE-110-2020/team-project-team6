package com.example.team_project_team6.ui.routes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RoutesFragment extends Fragment {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public RoutesViewModel routesViewModel = null;
    public RouteDetailsViewModel routeDetailsViewModel = null;

    private RecyclerView recyclerView;
    private RouteViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (routesViewModel == null) {
            routesViewModel = new ViewModelProvider(requireActivity()).get(RoutesViewModel.class);
        }

        if (routeDetailsViewModel == null) {
            routeDetailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        }

        View root = inflater.inflate(R.layout.fragment_routes, container, false);

        // check if previous screen is RouteDetailsFragment to prevent creation of another walk object
        RouteDetailsViewModel routeDetailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        routeDetailsViewModel.setIsWalkFromRouteDetails(false);



        final FloatingActionButton btNewRoute = root.findViewById(R.id.btNewRoute);

        // navigate to newRouteFragment when '+' button is pressed
        btNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Routes", "Clicked on '+' button");
                routeDetailsViewModel.setIsWalkFromRouteDetails(false);
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if (controller.getCurrentDestination().getId() == R.id.navigation_routes) {
                    routeDetailsViewModel.setIsWalkFromRouteDetails(true);
                    controller.navigate(R.id.action_navigation_routes_to_newRouteFragment);
                }
            }
        });

        recyclerView = root.findViewById(R.id.recycler_view_routes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RouteViewAdapter(routesViewModel.getAdapter().fbaseRclOptRoute());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        //bind_views();

        // Toggle favorite when favorite icon is pressed
        mAdapter.setOnFavoriteClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                boolean isFavorite = documentSnapshot.getBoolean("features.isFavorite");
                if(documentSnapshot.exists()){
                    routesViewModel.getAdapter().updateFavorite(documentSnapshot.getId(), !isFavorite);
                }
            }
        });

        // Navcontroller provides some cool animations and task stack management for us
        mAdapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Gson gson_route = new Gson();
                Route tmp = gson_route.fromJson(gson_route.toJson(documentSnapshot.getData()), Route.class);
                String id = documentSnapshot.getId();
                Toast.makeText(getActivity(),
                        "Position: " + position + " ID: " + id + " " + tmp.getName(), Toast.LENGTH_SHORT).show();
//                Route route = routesViewModel.getRouteAt(position);
//                if (route != null) {
//                    NavController controller = Navigation.findNavController(requireView());
//                    Log.d("Routes", "Clicked on route: " + route.getName());
//                    if (controller.getCurrentDestination().getId() == R.id.navigation_routes) {
//                        RouteDetailsViewModel route_details_view_model = ViewModelProviders.of(requireActivity()).get(RouteDetailsViewModel.class);
//                        route_details_view_model.setRoute(route);
//
//                        controller.navigate(R.id.action_navigation_routes_to_routeDetailsFragment);
//                    }
//                }
            }
        });

        return root;
    }

    public void bind_views() {
        routesViewModel.getRouteData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Route>>() {
            @Override
            public void onChanged(ArrayList<Route> routes) {
                mAdapter.updateData(routes);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
}