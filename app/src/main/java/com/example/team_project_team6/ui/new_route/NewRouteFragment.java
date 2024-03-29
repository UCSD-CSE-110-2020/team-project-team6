package com.example.team_project_team6.ui.new_route;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
public class NewRouteFragment extends Fragment {

    private NewRouteViewModel mNewRouteViewModel;
    @VisibleForTesting
    static RouteDetailsViewModel routeDetailsViewModel = null;

    private RadioButton radDiff;
    private RadioButton radHilly;
    private RadioButton radStreet;
    private RadioButton radEven;
    private RadioButton radLoop;
    private static final String TAG = FirebaseFirestore.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mNewRouteViewModel = new ViewModelProvider(requireActivity()).get(NewRouteViewModel.class);
        final View root =  inflater.inflate(R.layout.new_route_fragment, container, false);

        //hide bottom navigation bar
        View bottom_bar = requireActivity().findViewById(R.id.nav_view);
        if (bottom_bar != null) {
            bottom_bar.setVisibility(View.GONE);
        }

        final EditText txtRouteNme = root.findViewById(R.id.txtRouteName);
        final EditText txtStartingPoint = root.findViewById(R.id.txtStartingPoint);
        final EditText txtNotes = root.findViewById(R.id.txtNotes);

        final RadioGroup rgDiff = root.findViewById(R.id.radioGroup);
        final RadioGroup rgHilly = root.findViewById(R.id.radioGroup2);
        final RadioGroup rgStreet = root.findViewById(R.id.radioGroup3);
        final RadioGroup rgEven = root.findViewById(R.id.radioGroup4);
        final RadioGroup rgLoop = root.findViewById(R.id.radioGroup5);

        final Button btDone = root.findViewById(R.id.btDone);

        if (routeDetailsViewModel == null) {
            routeDetailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        }

        rgDiff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radDiff = root.findViewById(checkedId);
                //Toast.makeText(getActivity(), "Selected: " + radDiff.getText(), Toast.LENGTH_SHORT).show();
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

        rgLoop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedID if the RadioButton is selected
                radLoop = root.findViewById(checkedId);

            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtRouteNme.getText().toString().isEmpty()) {
                    txtRouteNme.requestFocus();
                    Toast alert =  Toast.makeText(getActivity(), getString(R.string.alert_route_name), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(),R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();

                } else {
                // save all of the features recorded by the user
                    Features features = new Features();

                    if(radDiff != null) {
                        String lvlType = radDiff.getText().toString();
                        features.findLevel(lvlType);
                    }

                    if(radLoop != null) {
                        String dirType = radLoop.getText().toString();
                        features.findDirectionType(dirType);
                    }

                    if(radHilly != null) {
                        String terrainType = radHilly.getText().toString();
                        features.findTerrain(terrainType);
                    }

                    features.setFavorite(false);

                    if(radStreet != null) {
                        String typeType = radStreet.getText().toString();
                        features.findType(typeType);
                    }

                    if (radEven != null) {
                        String surfaceType = radEven.getText().toString();
                        features.findSurface(surfaceType);
                    }

                    Log.i("value of Features in NewRouteFragment", features.toString());

                    Route route = new Route();


                    // if a new route is being created after redirection from the walk fragment, retrieve
                    // the walk's data to save inside the route
                    if (!routeDetailsViewModel.getIsWalkFromRouteDetails()) {
                        Walk walk = mNewRouteViewModel.getWalk();
                        if (walk != null) {
                            route.setWalk(walk);
                            route.setLastStartDate(walk.getStartTime().getTime());
                        }

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

                    mNewRouteViewModel.saveRoute(route); // save route to SharedPreferences

                    //come back to route
                    NavController controller = Navigation.findNavController(requireView());
                    if (controller.getCurrentDestination().getId() == R.id.newRouteFragment) {
                        controller.navigate(R.id.action_newRouteFragment_to_navigation_routes);
                    }
                }
            }
        });



        return root;
    }

    @Override
    public void onStop(){
        super.onStop();
        //showing up bottom bar
        View bottom_bar = requireActivity().findViewById(R.id.nav_view);
        if (bottom_bar != null) {
            bottom_bar.setVisibility(View.VISIBLE);
        }
    }
}