package com.example.team_project_team6.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class RoutesFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public RoutesViewModel routesViewModel = null;
    public RouteDetailsViewModel routeDetailsViewModel = null;

    private RecyclerView recyclerView;
    private RouteViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TabLayout mTabLayout;

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

        mAdapter = new RouteViewAdapter();
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        bind_views();

        // Toggle favorite when favorite icon is pressed
        mAdapter.setOnFavoriteClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    Features feature = route.getFeatures();
                    feature.setFavorite(!feature.isFavorite());
                    route.setFeatures(feature);

                    routesViewModel.updateRouteAt(position, route);
                    bind_views();
                }
            }
        });

        // Navcontroller provides some cool animations and task stack management for us
        mAdapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    NavController controller = Navigation.findNavController(requireView());
                    Log.d("Routes", "Clicked on route: " + route.getName());
                    if (controller.getCurrentDestination().getId() == R.id.navigation_routes) {
                        RouteDetailsViewModel route_details_view_model = ViewModelProviders.of(requireActivity()).get(RouteDetailsViewModel.class);
                        route_details_view_model.setRoute(route);

                        controller.navigate(R.id.action_navigation_routes_to_routeDetailsFragment);
                    }
                }
            }
        });

        mTabLayout = root.findViewById(R.id.routeTabLayout);
        mTabLayout.addOnTabSelectedListener(this);

        return root;
    }

    public void bind_views() {
        routesViewModel.getRouteData().observe(getViewLifecycleOwner(), routes -> {
            routesViewModel.updateMRoutes(routes);
            mAdapter.updateData(routes);
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        /* Really the best way to do this would be to have the route details page in
         *  a separate view. Then we can just bind each tab to its own view instead of
         *  checking the tab position like this. But this is good enough for now.
         */
        
        // Clear list of routes
        routesViewModel.updateMRoutes(new ArrayList<>());
        mAdapter.updateData(new ArrayList<>());
        mAdapter.notifyDataSetChanged();

        // Set to initial mode and load team/individual routes
        mAdapter.setTeamView(tab.getPosition() != 0);
        routesViewModel.setTeamView(tab.getPosition() != 0);
        bind_views();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Do nothing
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Do nothing
    }
}