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
import android.widget.Button;

import com.example.team_project_team6.R;

public class SendTeamRequestFragment extends Fragment {
    @VisibleForTesting
    static TeamViewModel teamViewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_send_team_request, container, false);

        final Button btSendInvite = root.findViewById(R.id.bt_invite);

        // navigate to team_fragment when send invite button is pressed
        btSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("go back to team", "Clicked on 'send invite' button");
                NavController controller = NavHostFragment.findNavController(requireParentFragment());
                if (controller.getCurrentDestination().getId() == R.id.sendTeamRequestFragment) {
                    controller.navigate(R.id.R_id_SendTeamRequestFragment_to_action_teamFragment_);
                }
            }
        });

        return root;
    }
}
