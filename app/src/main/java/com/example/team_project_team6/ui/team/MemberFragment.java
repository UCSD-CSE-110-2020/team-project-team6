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
import android.widget.ListView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.routes.RoutesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberFragment extends Fragment {
    private TeamViewModel teamViewModel;
    private ArrayAdapter mfAdapter;
    private String[] teamMemberArray;

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
        return root;
    }
}
