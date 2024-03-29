package com.example.team_project_team6.ui.team;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.ProposedWalk;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import java.util.Calendar;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SetProposedDateFragment extends Fragment {
    @VisibleForTesting
    static TeamViewModel teamViewModel = null;

    DatePickerDialog dpicker;
    TimePickerDialog tpicker;
    EditText dateEdit;
    EditText timeEdit;
    Button btDoneSet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "Creating set proposed date fragment");

        if (teamViewModel == null) {
            teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_set_proposed_date, container, false);

        dateEdit = (EditText) root.findViewById(R.id.date_edit);
        dateEdit.setInputType(InputType.TYPE_NULL);
        timeEdit = (EditText) root.findViewById(R.id.time_edit);
        timeEdit.setInputType(InputType.TYPE_NULL);
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicked edit time button.");
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                tpicker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                Log.i(TAG, "Setting time inside Time Picker");
                                String sHourStr = sHour < 10 ? ("0" + sHour) : Integer.toString(sHour);
                                String sMinuteStr = sMinute < 10 ? ("0" + sMinute) : Integer.toString(sMinute);
                                timeEdit.setText(sHourStr + ":" + sMinuteStr);
                            }
                        }, hour, minutes, true);
                tpicker.show();
            }
        });
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicked edit date button.");
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dpicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.i(TAG, "Setting date inside Date Picker");
                                String dayOfMonthStr = dayOfMonth < 10 ? ("0" + dayOfMonth) : Integer.toString(dayOfMonth);
                                String monthOfYearStr = monthOfYear + 1 < 10 ? ("0" + (monthOfYear + 1)) : Integer.toString(monthOfYear + 1);
                                dateEdit.setText(monthOfYearStr + "/" + dayOfMonthStr + "/" + year);
                            }
                        }, year, month, day);
                dpicker.show();
            }
        });

        btDoneSet = root.findViewById(R.id.bt_done_set);
        btDoneSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Clicked done button in SetProposedDataFragment");

                if(dateEdit.getText().toString().isEmpty()) {
                    dateEdit.requestFocus();
                    Toast alert =  Toast.makeText(root.getContext(), getString(R.string.alert_set_date), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(),R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();

                } else if(timeEdit.getText().toString().isEmpty()) {
                    dateEdit.requestFocus();
                    Toast alert =  Toast.makeText(root.getContext(), getString(R.string.alert_set_time), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(),R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();
                } else {
                    Log.i(TAG, "Saving proposed walk to Firebase");
                    // save info to firebase
                    RouteDetailsViewModel routeDetailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
                    Route route = routeDetailsViewModel.getRoute();

                    Log.i(TAG, "Route exists: " + (route != null));
                    ProposedWalk proposedWalk = new ProposedWalk();
                    proposedWalk.setpRoute(route);
                    proposedWalk.setpDayMonthYearDate(dateEdit.getText().toString());
                    proposedWalk.setpHourSecondTime(timeEdit.getText().toString());
                    teamViewModel.sendProposedWalk(proposedWalk);

                    NavController controller = Navigation.findNavController(requireView());
                    // navigate to proposed walk fragment
                    if (controller.getCurrentDestination().getId() == R.id.setProposedDate) {
                        Log.i(TAG, "Navigating from setProposedDateFragment to teamFragment");
                        controller.navigate(R.id.navigate_SetDateFragment_to_teamFragment);
                    }
                }
            }
        });
        return root;
    }
}
