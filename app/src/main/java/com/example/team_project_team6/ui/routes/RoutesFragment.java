package com.example.team_project_team6.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.example.team_project_team6.ui.new_route.NewRouteFragment;
import com.example.team_project_team6.ui.routes.RouteViewAdapter;
import com.example.team_project_team6.ui.routes.RoutesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RoutesFragment extends Fragment {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public RoutesViewModel routesViewModel = null;

    private RecyclerView recyclerView;
    private RouteViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (routesViewModel == null) {
            routesViewModel = new ViewModelProvider(requireActivity()).get(RoutesViewModel.class);
        }

        View root = inflater.inflate(R.layout.fragment_routes, container, false);

        final MainActivity mainActivity = (MainActivity) getActivity();
        // check if previous screen is RouteDetailsFragment to prevent creation of another walk object
        mainActivity.setIsWalkFromRouteDetails(false);

        final FloatingActionButton btNewRoute = root.findViewById(R.id.btNewRoute);

        btNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewRouteFragment ftNewRoute = new NewRouteFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.setCustomAnimations(android.R.animator.fade_in,
//                        android.R.animator.fade_out);
                ft.addToBackStack(null);
                ft.replace(R.id.nav_host_fragment, ftNewRoute);

                ft.commit();
            }
        });

        recyclerView = root.findViewById(R.id.recycler_view_routes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RouteViewAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        bind_views();

        mAdapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    NavController controller = NavHostFragment.findNavController(requireParentFragment());
                    Log.d("Routes", "Clicked on route: " + route.getName());
                    if (controller.getCurrentDestination().getId() == R.id.navigation_routes) {
                        RouteDetailsViewModel route_details_view_model = ViewModelProviders.of(requireActivity()).get(RouteDetailsViewModel.class);
                        route_details_view_model.setRoute(route);

                        controller.navigate(R.id.action_navigation_routes_to_routeDetailsFragment);
                    }
                }
            }
        });

        // Navcontroller provides some cool animations and task stack management for us
        mAdapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    NavController controller = NavHostFragment.findNavController(requireParentFragment());
                    Log.d("Routes", "Clicked on route: " + route.getName());
                    if (controller.getCurrentDestination().getId() == R.id.navigation_routes) {
                        RouteDetailsViewModel route_details_view_model = ViewModelProviders.of(requireActivity()).get(RouteDetailsViewModel.class);
                        route_details_view_model.setRoute(route);

                        controller.navigate(R.id.action_navigation_routes_to_routeDetailsFragment);
                    }
                }
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
}