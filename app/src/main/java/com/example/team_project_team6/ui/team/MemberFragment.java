package com.example.team_project_team6.ui.team;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.TeamMember;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MemberFragment extends Fragment {


    @VisibleForTesting
    static TeamViewModel teamViewModel = null;

    private Button btnAcceptInvite;
    private Button btnDeclineInvite;
    private TextView txtInviterName;
    private ListView listViewAccepted;
    private ListView listViewInvited;
    private TeamArrayAdapter mfAdapterAccepted;
    private TeamArrayAdapter mfAdapterInvited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }

        View root = inflater.inflate(R.layout.fragment_members, container, false);

        listViewAccepted = (ListView) root.findViewById(R.id.list_team_members);
        listViewInvited = (ListView) root.findViewById(R.id.list_invited_team_members);

        btnAcceptInvite = root.findViewById(R.id.btn_accept_invite);
        btnDeclineInvite = root.findViewById(R.id.btn_decline_invite);
        txtInviterName = root.findViewById(R.id.txt_team_inviter_name);

        resetInviteSection();
        bind_views();

        final FloatingActionButton btNewInvite = root.findViewById(R.id.bt_invite_member);

        // navigate to send_tem_request when '+' button is pressed
        btNewInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("send_team_request", "Clicked on '+' button");
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if (controller.getCurrentDestination().getId() == R.id.navigation_team) {
                    Log.i("member","we are in the member fragment");
                    controller.navigate(R.id.action_teamFragment_to_SendTeamRequestFragment);
                }
            }
        });

        btnAcceptInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("member fragment", "accepted invite");
                resetInviteSection();
                teamViewModel.setInviteIsAccepted(true);
            }
        });

        btnDeclineInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("member fragment", "declined invite");
                resetInviteSection();
                teamViewModel.setInviteIsAccepted(false);
            }
        });
        return root;
    }

    public void bind_views() {
        teamViewModel.getTeamMemberData().observe(getViewLifecycleOwner(), new Observer<ArrayList<TeamMember>>() {
            @Override
            public void onChanged(ArrayList<TeamMember> teamMembers) {
                Log.i("MemberFragment getTeamMemberData", "getting changed team member data");
                teamViewModel.updateMTeamMembers(teamMembers);
                mfAdapterAccepted = new TeamArrayAdapter(getActivity(), teamViewModel.getTeamMemberNames(teamMembers), false);
                listViewAccepted.setAdapter(mfAdapterAccepted);
                mfAdapterAccepted.notifyDataSetChanged();
            }
        });

        teamViewModel.getTeamInviterData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> memberMap) {
                Log.i("MemberFragment getTeamInviterData", "getting changed inviter data");
                if(!memberMap.isEmpty()) {
                    if (memberMap.get("toOrFrom").equals("from")) {
                        Log.i("MemberFragment getTeamInviterData", "found invitation from: " + memberMap.get("email"));
                        enableInviteSection(memberMap.get("name"));
                        teamViewModel.setHasPendingTeamInvite(true);
                    } else {
                        Log.i("MemberFragment getTeamInviterData", "found invitation to: " + memberMap.get("email"));
                        // teamViewModel.addTeamInviterName(memberMap.get("name"));
                        ArrayList<String> inviters = new ArrayList<>();
                        inviters.add(memberMap.get("name"));
                        mfAdapterInvited = new TeamArrayAdapter(getActivity(), inviters, true);
                        listViewInvited.setAdapter(mfAdapterInvited);
                        mfAdapterInvited.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void enableInviteSection(String teamName) {
        Log.i("MemberFragment enableInviteSection", "enabling invite section");
        btnAcceptInvite.setVisibility(View.VISIBLE);
        btnDeclineInvite.setVisibility(View.VISIBLE);
        txtInviterName.setText(teamName);
        txtInviterName.setTypeface(txtInviterName.getTypeface(), Typeface.BOLD_ITALIC);
    }

    private void resetInviteSection() {
        Log.i("MemberFragment enableInviteSection", "resetting invite section");
        txtInviterName.setText(R.string.default_inviter_name_none);
        txtInviterName.setTypeface(txtInviterName.getTypeface(), Typeface.NORMAL);
        btnAcceptInvite.setVisibility(View.INVISIBLE);
        btnDeclineInvite.setVisibility(View.INVISIBLE);
    }
}
