package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.w3c.dom.Text;

import java.util.Locale;

public class MockWalkFragment extends Fragment {

    public MockWalkFragment() {
        // Required empty public constructor
    }

    private AppCompatActivity mActivity;
    private WalkViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mock_walk, container, false);

        mActivity = (AppCompatActivity) requireActivity();
        setHasOptionsMenu(true);

        final Button mock_btStart = root.findViewById(R.id.mock_btStart);
        final Button mock_btAddSteps = root.findViewById(R.id.mock_btMockAddSteps);
        final TextView mock_lbDistance = root.findViewById(R.id.mock_lbDistance);
        final TextView mock_lbStep = root.findViewById(R.id.mock_lbStep);
        final TextView mock_lbTime = root.findViewById(R.id.mock_lbTime);
        final MainActivity mainActivity = (MainActivity) getActivity();

        mock_btAddSteps.setVisibility(View.INVISIBLE);

        mock_btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!dashboardViewModel.is_currently_walking().getValue()) {
                    mock_btAddSteps.setVisibility(View.VISIBLE);
                    dashboardViewModel.start_walking();
                    mock_btStart.setText(R.string.bt_stop);
                } else {
                    mock_btAddSteps.setVisibility(View.INVISIBLE);
                    dashboardViewModel.end_walking();
                    mock_btStart.setText(R.string.bt_start);

                    Toast.makeText(getActivity(), "time: " + mock_lbTime.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        SharedPreferences spfs = this.requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        final int heightInInches = spfs.getInt("user_height", -1);
        mock_btAddSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stepCountString = mock_lbStep.getText().toString();
                Long stepCountLong = parseStepCountStringToLong(stepCountString) + 500;
                double stepDistanceInMiles = getStepDistanceInMiles(heightInInches, stepCountLong);
                Log.d("onClick: stepDistance", "" + stepDistanceInMiles);
                mock_lbStep.setText(String.format(Locale.ENGLISH, "%d steps", stepCountLong));
                mock_lbDistance.setText(String.format(Locale.ENGLISH, "%.2f mi", stepDistanceInMiles));
            }
        });

        return root;
    }

    public double getStepDistanceInMiles(int heightInInches, Long stepCount) {
        double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;
        return (strideDistInFt * (double) stepCount) / 5280.0;
    }

    public Long parseStepCountStringToLong(String stepCountString) {
        Log.d("parseStepCountStringToLong", "Cast step count from String to Long");
        Long stepCountLong = 0l;
        if(stepCountString != null && stepCountString.contains(" ")) {
            stepCountString = stepCountString.substring(0, stepCountString.indexOf(' '));
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

        inflator.inflate(R.menu.action_bar, menu);
        super.onCreateOptionsMenu(menu, inflator);

        MenuItem mockWalkAction = menu.findItem(R.id.menu_mock_walk_action);
        mockWalkAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if(controller.getCurrentDestination().getId() == R.id.mockWalkFragment) {
                    controller.navigate(R.id.action_mockWalkFragment_to_navigation_walk);
                }

                return true;
            }
        });
    }
}
