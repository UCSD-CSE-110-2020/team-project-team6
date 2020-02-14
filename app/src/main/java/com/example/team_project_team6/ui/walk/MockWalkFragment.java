package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MockWalkFragment extends Fragment {

    public MockWalkFragment() {
        // Required empty public constructor
    }

    private WalkViewModel walkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        walkViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mock_walk, container, false);

        setHasOptionsMenu(true);

        final Button mock_btStart = root.findViewById(R.id.mock_btStart);
        final Button mock_btAddSteps = root.findViewById(R.id.mock_btMockAddSteps);
        final TextView mock_walkDist = root.findViewById(R.id.mock_lbDistance);
        final TextView mock_walkSteps = root.findViewById(R.id.mock_lbStep);
        final TextView mock_walkTime = root.findViewById(R.id.mock_lbTime);
        final Walk walk = new Walk(); // create walk object to store walk info

        // save reference to MainActivity and create object to handle SharedPreferences calls
        final MainActivity mainActivity = (MainActivity) getActivity();

        final SaveData saveData = new SaveData(mainActivity);

        mock_btAddSteps.setVisibility(View.INVISIBLE);

        // update text on walk screen button depending on whether or not user is on a walk
        if(walkViewModel.isCurrentlyWalking().getValue()) {
            mock_btStart.setText(R.string.bt_stop);
        } else {
            mock_btStart.setText(R.string.bt_start);
        }

        mock_btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!walkViewModel.isCurrentlyWalking().getValue()) {
                    mock_btAddSteps.setVisibility(View.VISIBLE);
                    walkViewModel.startWalking();

                    Calendar startTime = parseStopWatchTime(mock_walkTime.getText().toString());
                    walk.setStartTime(startTime);
                    mock_btStart.setText(R.string.bt_stop);
                } else {
                    mock_btAddSteps.setVisibility(View.INVISIBLE);
                    walkViewModel.endWalking();
                    mock_btStart.setText(R.string.mock_bt_start);

                    // get the duration, step count, and distance

                    String duration = getWalkDuration(walk.getStartTime(), mock_walkTime.getText().toString());
                    long stepCount = Long.parseLong(mock_walkSteps.getText().toString());
                    String[] distData = mock_walkDist.getText().toString().split("\\s+");
                    double distance = Double.parseDouble(distData[0]);

                    // save the information about the walk inside of the walk object
                    walk.setDuration(duration);
                    walk.setStep(stepCount);
                    walk.setDist(distance);

                    saveData.saveWalk(walk); // save walk into SharedPreferences

                    // reset values
                    mock_walkSteps.setText(R.string.walk_step_empty);
                    mock_walkDist.setText(R.string.dist_empty);

                    // show data when the walk is done!
                    Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "Steps: %d, Distance: %.2f,\nTime: %s", stepCount, distance, duration), Toast.LENGTH_LONG).show();
                }
            }
        });

        SharedPreferences spfs = this.requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        // get height from SharedPreferences and calculate stride distance
        final int heightInInches = saveData.getHeight();
        mock_btAddSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stepCountString = mock_walkSteps.getText().toString();
                Long stepCountLong = parseStepCountStringToLong(stepCountString) + 500;
                double stepDistanceInMiles = getStepDistanceInMiles(heightInInches, stepCountLong);
                Log.d("onClick: stepDistance", "" + stepDistanceInMiles);
                mock_walkSteps.setText(String.format(Locale.ENGLISH, "%d", stepCountLong));
                mock_walkDist.setText(String.format(Locale.ENGLISH, "%.2f mi", stepDistanceInMiles));
            }
        });

        return root;
    }

    public Calendar parseStopWatchTime(String stopWatchString) {
        Log.d("MockWalkFragment parseStopWatchTime on: ", stopWatchString);
        String zone = "PST";
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("PST"));

        int month = now.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR);
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        int ms = now.get(Calendar.MILLISECOND);
        int hour = 0;
        int minute = 0;
        int second = 0;

        String[] timeValues = stopWatchString.split(":");

        if(timeValues.length != 3) {
            Log.e("parseStopWatchTime incorrect size: ", "" + timeValues.length);
        } else {
            hour = Integer.parseInt(timeValues[0]);
            minute = Integer.parseInt(timeValues[1]);
            second = Integer.parseInt(timeValues[2]);
        }

        Calendar newCal = Calendar.getInstance();
        newCal.set(Calendar.YEAR, year);
        newCal.set(Calendar.MONTH, month);
        newCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        newCal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        newCal.set(Calendar.MILLISECOND, ms);
        newCal.set(Calendar.HOUR_OF_DAY, hour);
        newCal.set(Calendar.MINUTE, minute);
        newCal.set(Calendar.SECOND, second);

        return newCal;
    }

    public String getWalkDuration(Calendar startTimeCal, String endTimeString) {
        Calendar endTimeCal = parseStopWatchTime(endTimeString);

        int hours = endTimeCal.get(Calendar.HOUR_OF_DAY) - startTimeCal.get(Calendar.HOUR_OF_DAY);
        int minutes = endTimeCal.get(Calendar.MINUTE) - startTimeCal.get(Calendar.MINUTE);
        int seconds = endTimeCal.get(Calendar.SECOND) - startTimeCal.get(Calendar.SECOND);

        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(':');
        sb.append(minutes);
        sb.append(':');
        sb.append(seconds);

        return sb.toString();
    }

    /**
     * returns step distance in miles based off of height and step count
     */
    public double getStepDistanceInMiles(int heightInInches, Long stepCount) {
        double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;
        return (strideDistInFt * (double) stepCount) / 5280.0;
    }

    /**
     * returns step count in Long form using step count String
     */
    public Long parseStepCountStringToLong(String stepCountString) {
        Log.d("parseStepCountStringToLong", "Cast step count from String to Long");
        Long stepCountLong = 0l;
        if(stepCountString != null) {
            stepCountLong = Long.parseLong(stepCountString);
        }
        return stepCountLong;
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
