package com.example.team_project_team6.ui.walk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import java.util.Calendar;
import java.util.Locale;

public class WalkFragment extends Fragment {

    public WalkViewModel walkViewModel;
    public RouteDetailsViewModel routeDetailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walkViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        routeDetailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);
        setHasOptionsMenu(true);

        // create objects for necessary parts on the walk fragment page
        final TextView walkTitle = root.findViewById(R.id.text_walk);
        final Button btStart = root.findViewById(R.id.btStart);
        final TextView walkTime = root.findViewById(R.id.lbTime);
        final TextView walkSteps = root.findViewById(R.id.lbStep);
        final TextView walkDist = root.findViewById(R.id.lbDistance);

        final Walk walk = new Walk(); // create walk object to store walk info

        updateDisplay(btStart, walkDist, walkSteps, walkTime);

        if (!walkViewModel.getIsMockWalk() && routeDetailsViewModel.getIsWalkFromRouteDetails()) {
            walkTitle.setText(routeDetailsViewModel.getRoute().getName());
            runStartSequence(btStart);
        }

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walkViewModel.setIsMockWalk(false);
                // if user is not currently on a walk when button is pressed, initialize stopwatch,
                // set mode to walking, and get the time of the start of the walk
                if(!walkViewModel.isCurrentlyWalking().getValue()) {
                    runStartSequence(btStart);
                    walk.setStartTime(Calendar.getInstance());

                } else {
                    // if user presses button while walk is in progress, end the walk and stop the stopwatch
                    runStopSequence(btStart);
                    setWalkInfo(walk, walkTime, walkSteps, walkDist);

                    // show data when the walk is done!
                    // Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "Steps: %d, Distance: %f,\nTime: %s", stepCount, distance, duration), Toast.LENGTH_LONG).show();
                    navigateFromWalkFragment(walk);
                    routeDetailsViewModel.setIsWalkFromRouteDetails(false);
                    walkViewModel.resetToZero();
                }
            }
        });

        // get height from SharedPreferences and calculate stride distance
        final int heightInInches = walkViewModel.getHeight();

        // update the current step count and distance walked on the screen
        walkViewModel.getWalkSteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long num) {
                if(walkViewModel.isCurrentlyWalking().getValue()) {
                    if (num == null) {
                        num = 0L;
                    }

                    double dist = Walk.getStepDistanceInMiles(heightInInches, num);

                    walkSteps.setText(String.format(Locale.ENGLISH, "%d", num));
                    walkDist.setText(String.format(Locale.ENGLISH, "%.2f mi", dist));
                }

            }
        });

        // update time on stopwatch
        walkViewModel.getStopWatch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                walkTime.setText(s);
            }
        });

        return root;
    }


    public void updateDisplay(Button btStart, TextView walkTime, TextView walkSteps, TextView walkDist) {
        Log.i("WalkFragment", "update start/stop button");
        // update text on walk screen button depending on whether or not user is on a walk
        if(walkViewModel.isCurrentlyWalking().getValue()) {
            btStart.setText(R.string.bt_stop);
        } else {
            walkViewModel.resetToZero();
            btStart.setText(R.string.bt_start);
        }

    }

    public void runStartSequence(Button btStart) {
        Log.i("WalkFragment", "start walk");
        walkViewModel.runStopWatch();
        walkViewModel.startWalking();
        btStart.setText(R.string.bt_stop);
    }

    public void runStopSequence(Button btStart) {
        Log.i("WalkFragment", "stop walk");
        walkViewModel.stopWatch();
        walkViewModel.endWalking();
        btStart.setText(R.string.bt_start);
    }

    public void setWalkInfo(Walk walk, TextView lbStopWatch, TextView walkSteps, TextView walkDist) {
        Log.i("WalkFragment", "setWalkInfo");
        // get the duration, step count, and distance
        String duration = lbStopWatch.getText().toString();
        long stepCount = Long.parseLong(walkSteps.getText().toString());
        String[] distData = walkDist.getText().toString().split("\\s+");
        double distance = Double.parseDouble(distData[0]);

        // save the information about the walk inside of the walk object
        walk.setDuration(duration);
        walk.setStep(stepCount);
        walk.setDist(distance);
    }

    public void navigateFromWalkFragment(Walk walk) {
        Log.i("WalkFragment", "navigation");
        NavController controller = NavHostFragment.findNavController(requireParentFragment());
        // previous screen was route details screen
        if(routeDetailsViewModel.getIsWalkFromRouteDetails()) {
            Route route = routeDetailsViewModel.getRoute();
            route.setWalk(walk);
            route.setLastStartDate(walk.getStartTime().getTime());
            walkViewModel.saveRoute(route);
            // go to Routes screen
            if (controller.getCurrentDestination().getId() == R.id.navigation_walk) {
                controller.navigate(R.id.action_navigation_walk_to_navigation_routes);
            }
        } else {
            walkViewModel.saveWalk(walk); // save walk into SharedPreferences
            // go to newRouteFragment to save Walk in a Route object
            if (controller.getCurrentDestination().getId() == R.id.navigation_walk) {
                controller.navigate(R.id.action_navigation_walk_to_newRouteFragment);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("Opening fragment", "walkFragment");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator) {
        Log.i("WalkFragment", "creating options menu");

        inflator.inflate(R.menu.action_bar_mock_walk, menu);
        super.onCreateOptionsMenu(menu, inflator);

        // find action for navigating to the mock walk screen from action bar
        MenuItem mockWalkAction = menu.findItem(R.id.menu_mock_walk_action);
        mockWalkAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // navigate to mock walk screen on click
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if(controller.getCurrentDestination().getId() == R.id.navigation_walk) {
                    controller.navigate(R.id.action_navigation_walk_to_mockWalkFragment);
                }
                return true;
            }
        });
    }
}
