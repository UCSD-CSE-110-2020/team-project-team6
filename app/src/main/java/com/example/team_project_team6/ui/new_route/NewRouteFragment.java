package com.example.team_project_team6.ui.new_route;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.team_project_team6.R;

public class NewRouteFragment extends Fragment {

    private NewRouteViewModel mNewRouteModel;
    private RadioButton radDiff;
    private RadioButton radHilly;
    private RadioButton radStreet;
    private RadioButton radEven;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mNewRouteModel = new ViewModelProvider(requireActivity()).get(NewRouteViewModel.class);
        final View root =  inflater.inflate(R.layout.new_route_fragment, container, false);



        //hide bottom navigation bar
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);

        final EditText txtRouteNme = root.findViewById(R.id.txtRouteName);
        final EditText txtStartingPoint = root.findViewById(R.id.txtStartingPoint);
        final EditText txtNotes = root.findViewById(R.id.txtNotes);

        final RadioGroup rgDiff = root.findViewById(R.id.radioGroup);
        final RadioGroup rgHilly = root.findViewById(R.id.radioGroup2);
        final RadioGroup rgStreet = root.findViewById(R.id.radioGroup3);
        final RadioGroup rgEven = root.findViewById(R.id.radioGroup4);

        final Button btDone = root.findViewById(R.id.btDone);


        rgDiff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radDiff = root.findViewById(checkedId);
                Toast.makeText(getActivity(), "Selected: " + radDiff.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        rgHilly.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radHilly = root.findViewById(checkedId);
            }
        });

        rgStreet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radStreet = root.findViewById(checkedId);
            }
        });

        rgEven.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radEven = root.findViewById(checkedId);
            }
        });


        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data here


                //showing up bottom navigation bar
                getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
                //come back to route
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        return root;
    }


}