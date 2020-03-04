package com.example.team_project_team6.ui.team;

import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.example.team_project_team6.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MemberFragment extends Fragment {
    @VisibleForTesting
    static TeamViewModel teamViewModel = null;

    private ArrayAdapter mfAdapter;
    private String[] teamMemberArray;
    private Button btnAcceptInvite;
    private Button btnDeclineInvite;
    private TextView txtInviterName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }

        teamMemberArray = teamViewModel.getTeamMemberNameList().toArray(new String[0]);
        View root = inflater.inflate(R.layout.fragment_members, container, false);
        mfAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.single_item_list_view, teamMemberArray);
        ListView listView = (ListView) root.findViewById(R.id.list_team_members);
        listView.setAdapter(mfAdapter);
      
        btnAcceptInvite = root.findViewById(R.id.btn_accept_invite);
        btnDeclineInvite = root.findViewById(R.id.btn_decline_invite);
        txtInviterName = root.findViewById(R.id.txt_team_inviter_name);

        if(!teamViewModel.getHasPendingTeamInvite()) {
            btnAcceptInvite.setVisibility(View.INVISIBLE);
            btnDeclineInvite.setVisibility(View.INVISIBLE);
        }

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
                teamViewModel.setHasPendingTeamInvite(false);
            }
        });

        return root;
    }


}
