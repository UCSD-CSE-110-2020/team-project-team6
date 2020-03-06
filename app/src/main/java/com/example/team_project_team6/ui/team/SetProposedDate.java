package com.example.team_project_team6.ui.team;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

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

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;

import java.util.Calendar;


public class SetProposedDate extends Fragment {

    DatePickerDialog dpicker;
    TimePickerDialog tpicker;
    EditText dateEdit;
    EditText timeEdit;
    Button btDoneSet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_set_proposed_date, container, false);

        dateEdit=(EditText) root.findViewById(R.id.date_edit);
        dateEdit.setInputType(InputType.TYPE_NULL);
        timeEdit=(EditText) root.findViewById(R.id.time_edit);
        timeEdit.setInputType(InputType.TYPE_NULL);
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                tpicker = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                timeEdit.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                tpicker.show();
            }
        });
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dpicker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dpicker.show();
            }
        });

        btDoneSet = root.findViewById(R.id.bt_done_set);
        btDoneSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("set date fragment", "set date and time");

                if(dateEdit.getText().toString().isEmpty()) {
                    dateEdit.requestFocus();
                    Toast alert =  Toast.makeText(getActivity(), getString(R.string.alert_set_date), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(),R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();

                }
                else if(timeEdit.getText().toString().isEmpty()) {
                    dateEdit.requestFocus();
                    Toast alert =  Toast.makeText(getActivity(), getString(R.string.alert_set_time), Toast.LENGTH_SHORT);
                    int backgroundColor = ResourcesCompat.getColor(alert.getView().getResources(),R.color.colorAccent, null);
                    alert.getView().getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    alert.show();
                }
            }
        });
        return root;
    }
}
