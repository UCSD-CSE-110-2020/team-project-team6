package com.example.team_project_team6.ui.team;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team_project_team6.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SendTeamRequestFragment extends Fragment {
    @VisibleForTesting
    static TeamViewModel teamViewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "Creating send team request fragment");
        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_send_team_request, container, false);

        final Button btSendInvite = root.findViewById(R.id.bt_invite);
        final EditText gmailInvitation = root.findViewById(R.id.gmail_invitation_edit);

        // navigate to team_fragment when send invite button is pressed
        btSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Clicked on send invite button from SendTeamRequestFragment");
                if (gmailInvitation.getText().toString().isEmpty()) {
                    Log.i(TAG, "Gmail field is empty");
                    gmailInvitation.requestFocus();
                    Toast alert = Toast.makeText(getActivity(), getString(R.string.alert_gmail), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(), R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();
                } else {
                    Log.i(TAG, "Sending team request");

                    // send team request
                    teamViewModel.sendTeamRequest(gmailInvitation.getText().toString().trim());
                    teamViewModel.setIsMyProposedWalk(true);
                    NavController controller = Navigation.findNavController(requireView());
                    if (controller.getCurrentDestination().getId() == R.id.sendTeamRequestFragment) {
                        Log.i(TAG, "Navigating to team fragment");
                        controller.navigate(R.id.R_id_SendTeamRequestFragment_to_action_teamFragment);
                    }
                }
            }
        });

        return root;
    }
}
