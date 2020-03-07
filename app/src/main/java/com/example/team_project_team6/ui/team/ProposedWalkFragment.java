package com.example.team_project_team6.ui.team;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private ListView listView;

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

        setAllButtonsInvisible();
        setAllChangeableTextInvisible();
        bind_views();

        bt_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "schedule walk clicked");
                setInvisibleScheduleWithdraw();
                setInvisibleAcceptDecline();
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
            }
        });

        bt_declineRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "decline route clicked");
                toggleDeclineRoute();
            }
        });

        bt_declineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Proposed Walk fragment", "decline time clicked");
                toggleDeclineTime();
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

                // TODO: set button colors for current user for accept or decline buttons
                if(teamViewModel.isMyProposedWalk()) {
                    switch (memberGoingStatusMap.get("self")) {
                        case "accepted":
                            toggleAccept();
                            break;
                        case "declined route":
                            toggleDeclineRoute();
                            break;
                        case "declined time":
                            toggleDeclineTime();
                            break;
                    }
                }
            }
        });
    }

    public void toggleAccept() {
        Log.i(TAG, "Setting accept button colors");
        bt_acceptWalk.setBackgroundColor(getResources().getColor(R.color.design_default_color_secondary_variant));
        bt_declineRoute.setBackgroundColor(Color.GRAY);
        bt_declineTime.setBackgroundColor(Color.GRAY);
    }

    public void toggleDeclineRoute() {
        Log.i(TAG, "Setting decline route button colors");
        bt_acceptWalk.setBackgroundColor(Color.GRAY);
        bt_declineRoute.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        bt_declineTime.setBackgroundColor(Color.GRAY);

    }

    public void toggleDeclineTime() {
        Log.i(TAG, "Setting decline time button colors");
        bt_acceptWalk.setBackgroundColor(Color.GRAY);
        bt_declineRoute.setBackgroundColor(Color.GRAY);
        bt_declineTime.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public void populateProposedWalkElements(ProposedWalk proposedWalk) {
        Log.i(TAG, "Populating Proposed Walk elements");
        txt_date.setText(proposedWalk.getpDayMonthYearDate());
        txt_time.setText(proposedWalk.getpHourSecondTime());
        txt_routeName.setText(proposedWalk.getpRoute().getName());
        txt_miles.setText(Double.toString(proposedWalk.getpRoute().getWalk().getDist()) + " mi");
        txt_steps.setText(Long.toString(proposedWalk.getpRoute().getWalk().getStep()) + " steps");
        txt_startPoint.setText(proposedWalk.getpRoute().getStartPoint());
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
