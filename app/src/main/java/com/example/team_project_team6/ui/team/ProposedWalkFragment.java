package com.example.team_project_team6.ui.team;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.ProposedWalk;

import java.util.ArrayList;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ProposedWalkFragment extends Fragment {

    @VisibleForTesting
    static TeamViewModel teamViewModel = null;

    private TeamArrayAdapter mfAdapter;
    private ArrayList<String> goingStatusArray;
    private Button bt_acceptWalk;
    private Button bt_declineTime;
    private Button bt_declineRoute;
    private Button bt_schedule;
    private Button bt_withdraw;
    private TextView txt_routeName;
    private TextView txt_steps;
    private TextView txt_miles;
    private TextView txt_date;
    private TextView txt_time;
    private TextView txt_startPoint;
    private TextView txt_title;
    private ListView listView;
    private ProposedWalk currentWalk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "Creating ProposedWalkFragment");

        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }

        View root = inflater.inflate(R.layout.fragment_proposed_walk, container, false);

        goingStatusArray = teamViewModel.getAllMemberGoingStatusesExceptSelf();
        mfAdapter = new TeamArrayAdapter(getActivity(), goingStatusArray, false);
        listView = (ListView) root.findViewById(R.id.list_is_going);
        listView.setAdapter(mfAdapter);

        bt_acceptWalk = root.findViewById(R.id.bt_acceptWalk);
        bt_declineRoute = root.findViewById(R.id.bt_declineRoute);
        bt_declineTime = root.findViewById(R.id.bt_declineTime);
        bt_schedule = root.findViewById(R.id.bt_Schedule);
        bt_withdraw = root.findViewById(R.id.bt_withdraw);
        txt_date = root.findViewById(R.id.txt_proposed_date);
        txt_time = root.findViewById(R.id.txt_proposed_time);
        txt_routeName = root.findViewById(R.id.txt_proposed_route_Name);
        txt_miles = root.findViewById(R.id.txt_proposed_mile);
        txt_steps = root.findViewById(R.id.txt_proposed_steps);
        txt_startPoint = root.findViewById(R.id.txt_proposed_startingPoint);
        txt_title = root.findViewById(R.id.textView15);

        setAllButtonsInvisible();
        setAllChangeableTextInvisible();
        bind_views();

        bt_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "schedule walk clicked");
                setInvisibleScheduleWithdraw();
                setInvisibleAcceptDecline();
                switchToScheduledWalk();
            }
        });

        bt_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "withdraw walk clicked");
                setInvisibleScheduleWithdraw();
            }
        });

        bt_acceptWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "accept walk clicked");
                toggleAccept();
                teamViewModel.updateMemberGoingStatus("accepted");
            }
        });

        bt_declineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "decline time clicked");
                toggleDeclineTime();
                teamViewModel.updateMemberGoingStatus("declined time");
            }
        });

        bt_declineRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "decline route clicked");
                toggleDeclineRoute();
                teamViewModel.updateMemberGoingStatus("declined route");
            }
        });

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "Disabling ScrollView touch event interception for ListView");
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        return root;
    }

    private void switchToScheduledWalk() {
        if (currentWalk != null) {
            txt_title.setText(R.string.scheduled_walk_information);
            currentWalk.setScheduled(true);
            teamViewModel.sendProposedWalk(currentWalk);
        }
    }

    // Put everything that requires updating the UI based on the view model in here.
    // Required so that when we swap out the view model in the test code we can update
    // anything that depends on the view model. I actually found a different way that doesn't
    // require this by overwriting the fragment Factory class in the test code but this is OK
    // for a school project.
    public void bind_views() {
        teamViewModel.getProposedWalkData().observe(getViewLifecycleOwner(), new Observer<ProposedWalk>() {
           @Override
           public void onChanged(ProposedWalk proposedWalk) {
               Log.i("ProposedWalkFragment getProposedWalkData", "getting proposed walk data");
               teamViewModel.setIsMyProposedWalk(proposedWalk.getProposer().equals("yes"));
               teamViewModel.setHasProposedWalk(proposedWalk != null);
               Log.i(TAG, "teamViewModel has proposed walk: " + teamViewModel.getHasProposedWalk());
               toggleButtonVisibility();
               populateProposedWalkElements(proposedWalk);
               currentWalk = proposedWalk;

               if (proposedWalk != null && proposedWalk.isScheduled()) {
                   txt_title.setText(R.string.scheduled_walk_information);
                   currentWalk.setScheduled(true);
                   setInvisibleScheduleWithdraw();
               }
           }
        });

        teamViewModel.getAllMemberGoingData().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> memberGoingStatusMap) {
                Log.i("ProposedWalkFragment getMemberGoingData", "getting changed team member status data");
                teamViewModel.updateMemberGoingData(memberGoingStatusMap);
                mfAdapter = new TeamArrayAdapter(getActivity(), teamViewModel.getAllMemberGoingStatusesExceptSelf(), false);
                listView.setAdapter(mfAdapter);
                mfAdapter.notifyDataSetChanged();

                if(memberGoingStatusMap.get("self") != null && !teamViewModel.isMyProposedWalk()) {
                    switch (memberGoingStatusMap.get("self")) {
                        case "accepted":
                            toggleAccept();
                            break;
                        case "declined time":
                            toggleDeclineTime();
                            break;
                        case "declined route":
                            toggleDeclineRoute();
                            break;
                    }
                }
            }
        });
    }

    public void toggleAccept() {
        Log.i(TAG, "Setting accept button colors");
        bt_acceptWalk.getBackground().setColorFilter(bt_acceptWalk.getContext().getResources().getColor(R.color.design_default_color_secondary_variant), PorterDuff.Mode.MULTIPLY);
        bt_declineRoute.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        bt_declineTime.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }

    public void toggleDeclineRoute() {
        Log.i(TAG, "Setting decline route button colors");
        bt_acceptWalk.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        bt_declineRoute.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        bt_declineTime.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

    }

    public void toggleDeclineTime() {
        Log.i(TAG, "Setting decline time button colors");
        bt_acceptWalk.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        bt_declineRoute.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        bt_declineTime.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
    }

    public void populateProposedWalkElements(ProposedWalk proposedWalk) {
        Log.i(TAG, "Populating Proposed Walk elements");
        txt_date.setText(proposedWalk.getpDayMonthYearDate());
        txt_time.setText(proposedWalk.getpHourSecondTime());
        txt_routeName.setText(proposedWalk.getpRoute().getName());
        txt_miles.setText(Double.toString(proposedWalk.getpRoute().getWalk().getDist()) + " mi");
        txt_steps.setText(Long.toString(proposedWalk.getpRoute().getWalk().getStep()) + " steps");
        txt_startPoint.setText(proposedWalk.getpRoute().getStartPoint());
        txt_startPoint.setOnClickListener(v -> {
            String url = String.format("https://www.google.com/maps/search/%s/", proposedWalk.getpRoute().getStartPoint());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    public void toggleButtonVisibility() {
        if(!teamViewModel.getHasProposedWalk()) {
            Log.i(TAG, "User does not have a proposed walk in ProposedWalkFragment.");
            setAllButtonsInvisible();
        } else {
            Log.i(TAG, "isMyProposedWalk: " + teamViewModel.isMyProposedWalk());
            if (teamViewModel.isMyProposedWalk()) {
                Log.i(TAG, "Creating my proposed walk.");
                setAllChangeableTextVisible();
                bt_acceptWalk.setVisibility(View.INVISIBLE);
                bt_declineRoute.setVisibility(View.INVISIBLE);
                bt_declineTime.setVisibility(View.INVISIBLE);
                bt_schedule.setVisibility(View.VISIBLE);
                bt_withdraw.setVisibility(View.VISIBLE);
            } else {
                Log.i(TAG, "Creating another user's proposed walk.");
                setAllChangeableTextVisible();
                bt_acceptWalk.setVisibility(View.VISIBLE);
                bt_declineRoute.setVisibility(View.VISIBLE);
                bt_declineTime.setVisibility(View.VISIBLE);
                bt_schedule.setVisibility(View.INVISIBLE);
                bt_withdraw.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setInvisibleAcceptDecline() {
        Log.i(TAG, "Setting ProposedWalkFragment accept and decline buttons invisible.");
        bt_acceptWalk.setVisibility(View.INVISIBLE);
        bt_declineRoute.setVisibility(View.INVISIBLE);
        bt_declineTime.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleScheduleWithdraw() {
        Log.i(TAG, "Setting ProposedWalkFragment schedule and withdraw buttons invisible.");
        bt_schedule.setVisibility(View.INVISIBLE);
        bt_withdraw.setVisibility(View.INVISIBLE);
    }

    public void setAllButtonsInvisible() {
        Log.i(TAG, "Setting ProposedWalkFragment all textviews and buttons invisible.");
        bt_acceptWalk.setVisibility(View.INVISIBLE);
        bt_declineRoute.setVisibility(View.INVISIBLE);
        bt_declineTime.setVisibility(View.INVISIBLE);
        bt_schedule.setVisibility(View.INVISIBLE);
        bt_withdraw.setVisibility(View.INVISIBLE);
    }

    public void setAllChangeableTextInvisible() {
        txt_date.setVisibility(View.INVISIBLE);
        txt_time.setVisibility(View.INVISIBLE);
        txt_routeName.setVisibility(View.INVISIBLE);
        txt_miles.setVisibility(View.INVISIBLE);
        txt_steps.setVisibility(View.INVISIBLE);
        txt_startPoint.setVisibility(View.INVISIBLE);
    }

    public void setAllChangeableTextVisible() {
        txt_date.setVisibility(View.VISIBLE);
        txt_time.setVisibility(View.VISIBLE);
        txt_routeName.setVisibility(View.VISIBLE);
        txt_miles.setVisibility(View.VISIBLE);
        txt_steps.setVisibility(View.VISIBLE);
        txt_startPoint.setVisibility(View.VISIBLE);
    }
}
