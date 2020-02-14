package com.example.team_project_team6.ui.new_route;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.Walk;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.example.team_project_team6.ui.routes.RoutesViewModel;

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

        final MainActivity mainActivity = (MainActivity) getActivity();
        final SaveData saveData = new SaveData(mainActivity);

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
                // save all of the features recorded by the user
                Features features = new Features();
                features.setLevel(rgDiff.getCheckedRadioButtonId());
//                features.setDirectionType(); // TODO add radio button for this
                features.setTerrain(rgHilly.getCheckedRadioButtonId());
                features.setFavorite(false);
                features.setType(rgStreet.getCheckedRadioButtonId());
                features.setSurface(rgEven.getCheckedRadioButtonId());

                Route route = new Route();

                // if a new route is being created after redirection from the walk fragment, retrieve
                // the walk's data to save inside the route
                if (mainActivity.isCreateRouteFromWalk()) {
                    Walk walk = saveData.getWalk();
                    route.setWalk(walk);
                    route.setLastStartDate(walk.getStartTime());

                // otherwise, create a new walk object and save that inside route along with an unset
                // last start date
                } else {
                    route.setWalk(new Walk());
                    route.setLastStartDate(null);
                }

                // update remaining parameters of route object
                route.setName(txtRouteNme.getText().toString());
                route.setStartPoint(txtStartingPoint.getText().toString());
                route.setNotes(txtNotes.getText().toString());
                route.setFeatures(features);

                saveData.saveRoute(route); // save route to SharedPreferences

                //showing up bottom navigation bar
                getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

                //come back to route
                NavController controller = NavHostFragment.findNavController(requireParentFragment());

                if (controller.getCurrentDestination().getId() == R.id.newRouteFragment) {
                    controller.navigate(R.id.action_newRouteFragment_to_navigation_routes);
                }

            }
        });



        return root;
    }


}