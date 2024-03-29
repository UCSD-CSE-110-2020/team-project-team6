package com.example.team_project_team6.ui.route_details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.walk.WalkViewModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
        if (route.getStartPoint().isEmpty()) {
            root.findViewById(R.id.details_start).setVisibility(View.GONE);
            root.findViewById(R.id.details_start_label).setVisibility(View.GONE);
        }

        ((TextView) root.findViewById(R.id.details_start)).setText(route.getStartPoint());
        root.findViewById(R.id.details_start).setOnClickListener(v -> {
            String url = String.format("https://www.google.com/maps/search/%s/", route.getStartPoint());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

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
            Date dateLastWalked = route.getLastStartDate();
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

        final Button proposeWalk = root.findViewById(R.id.bt_propose);
        proposeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Propose walk button pressed in route details fragment");
                if (controller.getCurrentDestination().getId() == R.id.routeDetailsFragment) {
                    controller.navigate(R.id.action_routeDetailFragment_to_SetProposeDate);

                    mViewModel.setRoute(route);
                    Log.i("Setting route in routeDetailsViewModel", "route name: " + route.getName());
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

    // Bug in Google's support library (theoretically)
    @SuppressLint("RestrictedApi")
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
