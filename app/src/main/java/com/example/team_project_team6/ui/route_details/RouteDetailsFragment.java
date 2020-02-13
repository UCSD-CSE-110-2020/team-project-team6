package com.example.team_project_team6.ui.route_details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RouteDetailsFragment extends Fragment {

    private RouteDetailsViewModel mViewModel;
    private AppCompatActivity mActivity;
    private Route route;
    private boolean is_favorite;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(RouteDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_route_details, container, false);

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
        switch (route.getFeatures().getTerrain()) {
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
        switch (route.getFeatures().getTerrain()) {
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

        inflator.inflate(R.menu.action_bar, menu);
        super.onCreateOptionsMenu(menu, inflator);

        menu.findItem(R.id.menu_filled_star).setVisible(is_favorite);
        menu.findItem(R.id.menu_empty_star).setVisible(!is_favorite);
    }
}