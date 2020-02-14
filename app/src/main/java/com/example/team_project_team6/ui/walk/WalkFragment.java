package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;

import java.util.Locale;

public class WalkFragment extends Fragment {

    private WalkViewModel dashboardViewModel;
    private AppCompatActivity mActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        final TextView textView = root.findViewById(R.id.text_walk);
        final TextView lbStopWatch = root.findViewById(R.id.lbTime);
        final Button btStart = root.findViewById(R.id.btStart);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mActivity = (AppCompatActivity) requireActivity();
        setHasOptionsMenu(true);

        if(dashboardViewModel.is_currently_walking().getValue()) {
            btStart.setText(R.string.bt_stop);
        } else {
            btStart.setText(R.string.bt_start);
        }

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dashboardViewModel.is_currently_walking().getValue()) {
                    mainActivity.runStopWatch();
                    dashboardViewModel.start_walking();
                    btStart.setText(R.string.bt_stop);
                } else {
                    dashboardViewModel.end_walking();
                    btStart.setText(R.string.bt_start);
                    mainActivity.stopWatch();

                    //show data when the walk has done!
                    Toast.makeText(getActivity(), "time: " + lbStopWatch.getText().toString(), Toast.LENGTH_LONG).show();

                }
            }
        });

        final TextView walkSteps = root.findViewById(R.id.lbStep);
        final TextView walkDist = root.findViewById(R.id.lbDistance);
        SharedPreferences spfs = this.requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        final int heightInInches = spfs.getInt("user_height", -1);
        final double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;

         dashboardViewModel.getWalkSteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long num) {

                if (num == null) {
                    num = 0l;
                }

                double dist = strideDistInFt * num / 5280.0;

                walkSteps.setText(String.format(Locale.ENGLISH, "%d steps", num));
                walkDist.setText(String.format(Locale.ENGLISH, "%.2f mi", dist));
            }
        });

        dashboardViewModel.getStopWatch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lbStopWatch.setText(s);
            }
        });

        return root;
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
                if(controller.getCurrentDestination().getId() == R.id.navigation_walk) {
                    controller.navigate(R.id.action_navigation_walk_to_mockWalkFragment);
                }

                return true;
            }
        });
    }


}