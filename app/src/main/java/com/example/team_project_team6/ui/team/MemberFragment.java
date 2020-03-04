package com.example.team_project_team6.ui.team;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.ui.routes.RoutesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MemberFragment extends Fragment {
    private TeamViewModel teamViewModel;
    private TeamArrayAdapter mfAdapter;
    private Button btnAcceptInvite;
    private Button btnDeclineInvite;
    private TextView txtInviterName;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }
        bind_views();
        View root = inflater.inflate(R.layout.fragment_members, container, false);

        listView = (ListView) root.findViewById(R.id.list_team_members);

        btnAcceptInvite = root.findViewById(R.id.btn_accept_invite);
        btnDeclineInvite = root.findViewById(R.id.btn_decline_invite);
        txtInviterName = root.findViewById(R.id.txt_team_inviter_name);

        btnAcceptInvite.setVisibility(View.INVISIBLE);
        btnDeclineInvite.setVisibility(View.INVISIBLE);

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
                txtInviterName.setText(R.string.default_inviter_name_none);
                btnAcceptInvite.setVisibility(View.INVISIBLE);
                btnDeclineInvite.setVisibility(View.INVISIBLE);
            }
        });
        return root;
    }

    public void bind_views() {
        teamViewModel.getTeamMemberData().observe(getViewLifecycleOwner(), new Observer<ArrayList<TeamMember>>() {
            @Override
            public void onChanged(ArrayList<TeamMember> teamMembers) {
                teamViewModel.updateMTeamMembers(teamMembers);

                mfAdapter = new TeamArrayAdapter(getActivity(), teamViewModel.getTeamMemberNames());
                listView.setAdapter(mfAdapter);
                mfAdapter.notifyDataSetChanged();
            }
        });
/*
        teamViewModel.getTeamInviterData().observe(getViewLifecycleOwner(), new Observer<TeamMember>()) {
            @Override
            public void onChanged(TeamMember inviter) {
            }
        }
 */
    }
}
