package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;
import com.example.team_project_team6.ui.home.HomeViewModel;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MockWalkFragment extends Fragment {

    private WalkViewModel walkViewModel;
    private RouteDetailsViewModel routeDetailsViewModel;
    private HomeViewModel homeViewModel;
    private MainActivity mainActivity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walkViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        routeDetailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        mainActivity = (MainActivity) getActivity();

        View root = inflater.inflate(R.layout.fragment_mock_walk, container, false);
        setHasOptionsMenu(true);
        walkViewModel.setIsMockWalk(true);

        final TextView mock_walkTitle = root.findViewById(R.id.mock_text_new_walk);
        final Button mock_btStart = root.findViewById(R.id.mock_btStart);
        final Button mock_btAddSteps = root.findViewById(R.id.mock_btMockAddSteps);
        final TextView mock_walkDist = root.findViewById(R.id.mock_lbDistance);
        final TextView mock_walkSteps = root.findViewById(R.id.mock_lbStep);
        final TextView mock_walkTime = root.findViewById(R.id.mock_lbTime);

        final Walk walk = new Walk(); // create walk object to store walk info

        updateDisplay(mock_btStart, mock_btAddSteps, mock_walkDist, mock_walkSteps, mock_walkTime);

        if(routeDetailsViewModel.getIsWalkFromRouteDetails()) {
            mock_walkTitle.setText(routeDetailsViewModel.getRoute().getName());
            runStartSequence(mock_btStart, mock_btAddSteps);
        }

        SharedPreferences spfs = this.requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        // get height from SharedPreferences and calculate stride distance
        final int heightInInches = walkViewModel.getHeight();
        mock_btAddSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stepCountString = mock_walkSteps.getText().toString();
                Long stepCountLong = Walk.parseStepCountStringToLong(stepCountString) + 500;
                double stepDistanceInMiles = Walk.getStepDistanceInMiles(heightInInches, stepCountLong);
                Log.d("onClick: stepDistance", "" + stepDistanceInMiles);
                mock_walkSteps.setText(String.format(Locale.ENGLISH, "%d", stepCountLong));
                mock_walkDist.setText(String.format(Locale.ENGLISH, "%.2f mi", stepDistanceInMiles));
            }
        });

        mock_btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if user is not currently on a walk when button is pressed,
                // set mode to walking, and get the time of the start of the walk
                if(!walkViewModel.isCurrentlyWalking().getValue()) {
                    runStartSequence(mock_btStart, mock_btAddSteps);

                    Calendar startTime = Walk.convertWalkStartTimeStringToCalendar(
                            Calendar.getInstance(TimeZone.getTimeZone("PST")),
                            mock_walkTime.getText().toString());
                    walk.setStartTime(startTime);

                } else {
                    // if user presses button while walk is in progress, end the walk
                    runStopSequence(mock_btStart, mock_btAddSteps);
                    setWalkInfo(walk, mock_walkDist, mock_walkSteps, mock_walkTime);

                    // show data when the walk is done!
                    // Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "Steps: %d, Distance: %.2f,\nTime: %s", stepCount, distance, duration), Toast.LENGTH_LONG).show();
                    navigateFromWalkFragment(walk);
                    routeDetailsViewModel.setIsWalkFromRouteDetails(false);
                    walkViewModel.resetToZero();
                }
            }
        });

        return root;
    }

    public void updateDisplay(Button btStart, Button mock_btAddSteps, TextView mock_walkTime, TextView mock_walkSteps, TextView mock_walkDist) {
        Log.i("MockWalkFragment", "update start/stop button");
        // update text on walk screen button depending on whether or not user is on a walk
        if(walkViewModel.isCurrentlyWalking().getValue()) {
            mock_btAddSteps.setVisibility(View.VISIBLE);
            btStart.setText(R.string.bt_stop);
        } else {
            mock_btAddSteps.setVisibility(View.INVISIBLE);
            walkViewModel.resetToZero();
            btStart.setText(R.string.bt_start);
        }
    }

    public void runStartSequence(Button mock_btStart, Button mock_btAddSteps) {
        Log.i("MockWalkFragment", "start walk");
        mock_btAddSteps.setVisibility(View.VISIBLE);
        walkViewModel.startWalking();
        mock_btStart.setText(R.string.bt_stop);
    }

    public void runStopSequence(Button btStart, Button mock_btAddSteps) {
        Log.i("MockWalkFragment", "stop walk");
        mock_btAddSteps.setVisibility(View.INVISIBLE);
        walkViewModel.endWalking();
        btStart.setText(R.string.bt_start);
        // reset values
        walkViewModel.resetToZero();
    }

    public void setWalkInfo(Walk walk, TextView mock_walkDist, TextView mock_walkSteps, TextView mock_walkTime) {
        Log.i("MockWalkFragment", "setWalkInfo");
        // get the duration, step count, and distance
        String duration = mock_walkTime.getText().toString();
        long stepCount = Long.parseLong(mock_walkSteps.getText().toString());
        String[] distData = mock_walkDist.getText().toString().split("\\s+");
        double distance = Double.parseDouble(distData[0]);

        // save the information about the walk inside of the walk object
        walk.setDuration(duration);
        walk.setStep(stepCount);
        walk.setDist(distance);

        mainActivity.stopAsyncTaskRunner();

        homeViewModel.updateDailySteps(homeViewModel.getDailyStepCount() + stepCount);
        Log.e("Setting homeViewModel step count in mockWalk","step count: " + (homeViewModel.getDailyStepCount() + stepCount));
    }

    public void navigateFromWalkFragment(Walk walk) {
        Log.i("MockWalkFragment", "navigation");
        NavController controller = Navigation.findNavController(requireView());
        if(routeDetailsViewModel.getIsWalkFromRouteDetails()) {
            Route route = routeDetailsViewModel.getRoute();
            route.setWalk(walk);
            route.setLastStartDate(walk.getStartTime().getTime());
            walkViewModel.saveRoute(route);
            // go to Routes screen
            if (controller.getCurrentDestination().getId() == R.id.mockWalkFragment) {
                controller.navigate(R.id.action_mockWalkFragment_to_navigation_routes);
            }
        } else {
            walkViewModel.saveWalk(walk); // save walk into SharedPreferences
            // go to newRouteFragment to save Walk in a Route object
            if (controller.getCurrentDestination().getId() == R.id.mockWalkFragment) {
                controller.navigate(R.id.action_mockWalkFragment_to_newRouteFragment);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("Opening fragment", "MockWalkFragment");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator) {
        Log.d("MockWalkFragment", "creating options menu");

        inflator.inflate(R.menu.action_bar_mock_walk, menu);
        super.onCreateOptionsMenu(menu, inflator);

        // find action for navigating to the mock walk screen from action bar
        MenuItem mockWalkAction = menu.findItem(R.id.menu_mock_walk_action);
        mockWalkAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // navigate to mock walk screen on click
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if(controller.getCurrentDestination().getId() == R.id.mockWalkFragment) {
                    controller.navigate(R.id.action_mockWalkFragment_to_navigation_walk);
                }
                return true;
            }
        });
    }
}
