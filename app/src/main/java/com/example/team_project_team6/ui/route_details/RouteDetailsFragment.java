package com.example.team_project_team6.ui.route_details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.walk.WalkViewModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RouteDetailsFragment extends Fragment {

    public RouteDetailsViewModel mViewModel;
    private WalkViewModel walkViewModel;
    private AppCompatActivity mActivity;
    private Route route;
    private boolean is_favorite;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        walkViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_route_details, container, false);

        mActivity = (AppCompatActivity) requireActivity();
        route = mViewModel.getRoute();

        // needed to get actionbar options menu to update
        setHasOptionsMenu(true);

        // set dist and steps
        String step_text = String.format(Locale.ENGLISH, "%d steps", route.getWalk().getStep());
        String dist_text = String.format(Locale.ENGLISH, "%.2f mi", route.getWalk().getDist());
        ((TextView)root.findViewById(R.id.details_steps)).setText(step_text);
        ((TextView)root.findViewById(R.id.details_dist)).setText(dist_text);

        // duration
        ((TextView) root.findViewById(R.id.details_duration)).setText(route.getWalk().getDuration());

        // starting location
        String starting = String.format(Locale.ENGLISH, "Starting Location: %s", route.getStartPoint());
        ((TextView) root.findViewById(R.id.details_last_starting)).setText(starting);

        final FlexboxLayout flexbox = root.findViewById(R.id.details_features_flex);

        // difficulty
        final TextView difficulty = (TextView)root.findViewById(R.id.detail_features_difficulty);
        switch (route.getFeatures().getLevel()) {
            case 1:
                difficulty.setText(R.string.details_features_easy);
                break;
            case 2:
                difficulty.setText(R.string.details_features_medium);
                break;
            case 3:
                difficulty.setText(R.string.details_features_hard);
                break;
            default:
                flexbox.removeView(difficulty);
        }

        // one way or circular
        final TextView type = (TextView)root.findViewById(R.id.detail_features_type);
        switch(route.getFeatures().getDirectionType()) {
            case 1:
                type.setText(R.string.details_features_loop);
                break;
            case 2:
                type.setText(R.string.details_features_outandback);
                break;
            default:
                flexbox.removeView(type);
        }

        // flat/hilly
        final TextView hilly = (TextView)root.findViewById(R.id.detail_features_hilly);
        switch (route.getFeatures().getTerrain()) {
            case 1:
                hilly.setText(R.string.details_features_flat);
                break;
            case 2:
                hilly.setText(R.string.details_features_hilly);
                break;
            default:
                flexbox.removeView(hilly);
        }

        // street/trail
        final TextView street = (TextView)root.findViewById(R.id.detail_features_street);
        switch (route.getFeatures().getType()) {
            case 1:
                street.setText(R.string.details_features_street);
                break;
            case 2:
                street.setText(R.string.details_features_trail);
                break;
            default:
                flexbox.removeView(street);
        }

        // even/not even
        final TextView evenness = (TextView)root.findViewById(R.id.detail_features_eveness);
        switch (route.getFeatures().getSurface()) {
            case 1:
                evenness.setText(R.string.details_features_even);
                break;
            case 2:
                evenness.setText(R.string.details_features_uneven);
                break;
            default:
                flexbox.removeView(evenness);
        }

        // last completed
        final TextView last_start = root.findViewById(R.id.details_last_completed);
        if (route.getLastStartDate() != null) {
            Date dateLastWalked = route.getLastStartDate().getTime();
            SimpleDateFormat format = new SimpleDateFormat("h:mm a. MMM d, yyyy", Locale.ENGLISH);
            last_start.setText(format.format(dateLastWalked));
        }

        // Notes
        final AppCompatEditText notes = root.findViewById(R.id.details_notes);
        notes.setText(route.getNotes());
        notes.setEnabled(false); // make it non editable

        // navigate to walk screen and start a walk
        final NavController controller = NavHostFragment.findNavController(this);
        final FloatingActionButton btnDetailsStartWalk = root.findViewById(R.id.details_btn_start_walk);

        if(walkViewModel.isWalking()) {
            btnDetailsStartWalk.setEnabled(false);
            btnDetailsStartWalk.setSupportBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        btnDetailsStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int destinationId = (walkViewModel.getIsMockWalk()) ?
                        R.id.action_routeDetailsFragment_to_mockWalkFragment :
                        R.id.action_routeDetailsFragment_to_navigation_walk;

                if (controller.getCurrentDestination().getId() == R.id.routeDetailsFragment) {
                    controller.navigate(destinationId);

                    mViewModel.setIsWalkFromRouteDetails(true);
                    mViewModel.setRoute(route);
                }

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("Opening route details for ", route.getName());
        set_activity_name(route.getName());

        is_favorite = route.getFeatures().isFavorite();
        refresh_options_menu();

        super.onActivityCreated(savedInstanceState);
    }

    private void set_activity_name(String title) {
        ActionBar supportActionBar = mActivity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    private void refresh_options_menu() {
        ActionBar supportActionBar = mActivity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.invalidateOptionsMenu();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator) {
        Log.d("Route_Details", "creating options menu");

        inflator.inflate(R.menu.action_bar_route_details, menu);
        super.onCreateOptionsMenu(menu, inflator);

        menu.findItem(R.id.menu_filled_star).setVisible(is_favorite);
        menu.findItem(R.id.menu_empty_star).setVisible(!is_favorite);
    }
}
