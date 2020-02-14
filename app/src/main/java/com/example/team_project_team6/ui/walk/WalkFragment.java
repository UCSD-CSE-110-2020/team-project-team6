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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;

import java.util.Calendar;
import java.util.Locale;

public class WalkFragment extends Fragment {

    private WalkViewModel walkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        walkViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);
        setHasOptionsMenu(true);

        // create objects for necessary parts on the walk fragment page
        final TextView lbStopWatch = root.findViewById(R.id.lbTime);
        final Button btStart = root.findViewById(R.id.btStart);
        final TextView walkSteps = root.findViewById(R.id.lbStep);
        final TextView walkDist = root.findViewById(R.id.lbDistance);

        final Walk walk = new Walk(); // create walk object to store walk info

        // save reference to MainActivity and create object to handle SharedPreferences calls
        final MainActivity mainActivity = (MainActivity) getActivity();
        final SaveData saveData = new SaveData(mainActivity);

        // update text on walk screen button depending on whether or not user is on a walk
        if(walkViewModel.isCurrentlyWalking().getValue()) {
            btStart.setText(R.string.bt_stop);
        } else {
            btStart.setText(R.string.bt_start);
        }

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if user is not currently on a walk when button is pressed, initialize stopwatch,
                // set mode to walking, and get the time of the start of the walk
                if(!walkViewModel.isCurrentlyWalking().getValue()) {
                    mainActivity.runStopWatch();
                    walkViewModel.startWalking();
                    walk.setStartTime(Calendar.getInstance());
                    btStart.setText(R.string.bt_stop);

                } else {
                    // if user presses button while walk is in progress, end the walk and stop the stopwatch
                    walkViewModel.endWalking();
                    btStart.setText(R.string.bt_start);
                    mainActivity.stopWatch();

                    // get the duration, step count, and distance
                    String duration = lbStopWatch.getText().toString();
                    long stepCount = Long.parseLong(walkSteps.getText().toString());
                    String[] distData = walkDist.getText().toString().split("\\s+");
                    double distance = Double.parseDouble(distData[0]);

                    // save the information about the walk inside of the walk object
                    walk.setDuration(duration);
                    walk.setStep(stepCount);
                    walk.setDist(distance);

                    saveData.saveWalk(walk); // save walk into SharedPreferences

                    // reset values
                    walkSteps.setText(R.string.walk_step_empty);
                    walkDist.setText(R.string.dist_empty);

                    // show data when the walk is done!
                    Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "Steps: %d, Distance: %f,\nTime: %s", stepCount, distance, duration), Toast.LENGTH_LONG).show();

                }
            }
        });

        // get height from SharedPreferences and calculate stride distance
        final int heightInInches = saveData.getHeight();

        // update the current step count and distance walked on the screen
        walkViewModel.getWalkSteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long num) {
                if (num == null) {
                    num = 0L;
                }

                double dist = getStepDistanceInMiles(heightInInches, num);

                walkSteps.setText(String.format(Locale.ENGLISH, "%d", num));
                walkDist.setText(String.format(Locale.ENGLISH, "%.2f mi", dist));
            }
        });

        // update time on stopwatch
        walkViewModel.getStopWatch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lbStopWatch.setText(s);
            }
        });

        return root;
    }

    public double getStepDistanceInMiles(int heightInInches, Long stepCount) {
        double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;
        return (strideDistInFt * (double) stepCount) / 5280.0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("Opening fragment", "mock walk");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator) {
        Log.d("Mock Walk", "creating options menu");

        inflator.inflate(R.menu.action_bar_mock_walk, menu);
        super.onCreateOptionsMenu(menu, inflator);

        MenuItem mockWalkAction = menu.findItem(R.id.menu_mock_walk_action);
        mockWalkAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if(controller.getCurrentDestination().getId() == R.id.navigation_walk) {
                    controller.navigate(R.id.action_navigation_walk_to_mockWalkFragment);
                }

                return true;
            }
        });
    }


}