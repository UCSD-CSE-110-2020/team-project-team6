package com.example.team_project_team6.ui.team;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team_project_team6.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendTeamRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendTeamRequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SendTeamRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendTeamRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendTeamRequestFragment newInstance(String param1, String param2) {
        SendTeamRequestFragment fragment = new SendTeamRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_send_team_request, container, false);

        final Button btSendInvite = root.findViewById(R.id.bt_invite);
        final EditText firstname_invitation = root.findViewById(R.id.firstname_invitation_edit);
        final EditText lastname_invitation = root.findViewById(R.id.lastname_invitation_edit);
        final EditText gmail_invitation = root.findViewById(R.id.gmail_invitation_edit);

        // navigate to team_fragment when send invite button is pressed
        btSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstname_invitation.getText().toString().isEmpty()) {
                    firstname_invitation.requestFocus();
                    Toast alert = Toast.makeText(getActivity(), getString(R.string.alert_firstname), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(), R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();
                } else if (lastname_invitation.getText().toString().isEmpty()) {
                    lastname_invitation.requestFocus();
                    Toast alert = Toast.makeText(getActivity(), getString(R.string.alert_lastname), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(), R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();
                } else if (gmail_invitation.getText().toString().isEmpty()) {
                    gmail_invitation.requestFocus();
                    Toast alert = Toast.makeText(getActivity(), getString(R.string.alert_gmail), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(), R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();
                }
                else{
                    Log.i("go back to team", "Clicked on 'send invite' button");
                    NavController controller = NavHostFragment.findNavController(requireParentFragment());
                    if (controller.getCurrentDestination().getId() == R.id.sendTeamRequestFragment) {
                        controller.navigate(R.id.R_id_SendTeamRequestFragment_to_action_teamFragment_);
                    }
                }
            }
        });

        return root;
    }
}
