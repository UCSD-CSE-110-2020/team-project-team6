package com.example.team_project_team6.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
<<<<<<< HEAD
<<<<<<< HEAD
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
=======
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
>>>>>>> dc1005e...  route label
=======
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
>>>>>>> dc1005e98c02ae1f0b9382b1b73572d402520d1d
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
<<<<<<< HEAD
<<<<<<< HEAD
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
=======
>>>>>>> dc1005e...  route label
=======
>>>>>>> dc1005e98c02ae1f0b9382b1b73572d402520d1d
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
<<<<<<< HEAD
<<<<<<< HEAD
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
=======
import com.example.team_project_team6.ui.new_route.NewRouteFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
>>>>>>> dc1005e...  route label
=======
import com.example.team_project_team6.ui.new_route.NewRouteFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
>>>>>>> dc1005e98c02ae1f0b9382b1b73572d402520d1d

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

        mAdapter.setOnFavoriteClickListener(new RouteViewAdapter.ClickListener() {
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

<<<<<<< HEAD
<<<<<<< HEAD
        // Navcontroller provides some cool animations and task stack management for us
        mAdapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
=======
=======
>>>>>>> dc1005e98c02ae1f0b9382b1b73572d402520d1d
        adapter.setOnItemClickListener(new RouteViewAdapter.ClickListener() {
>>>>>>> dc1005e...  route label
            @Override
            public void onItemClick(int position, View v) {
                Route route = routesViewModel.getRouteAt(position);
                if (route != null) {
                    NavController controller = NavHostFragment.findNavController(requireParentFragment());
                    Log.d("Routes", "Clicked on route: " + route.getName());
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