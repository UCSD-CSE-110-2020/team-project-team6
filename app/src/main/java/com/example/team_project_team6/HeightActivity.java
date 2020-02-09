package com.example.team_project_team6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class HeightActivity extends AppCompatActivity {

    private final int INCHES = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        final NumberPicker npFeet = findViewById(R.id.btFeetPicker);
        final NumberPicker npIn = findViewById(R.id.btInPicker);

        npFeet.setMinValue(0);
        npFeet.setMaxValue(8);

        npIn.setMinValue(0);
        npIn.setMaxValue(11);


        findViewById(R.id.btContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get height value
                int feet = npFeet.getValue();
                int in = npIn.getValue();

                //calculate height (in)
                int totalIN = feet * INCHES + in;

                //save date with SharedPreferences
                SharedPreferences spfs = getSharedPreferences("user_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = spfs.edit();
                editor.putInt("user_height", totalIN);
                editor.apply();

                //show toast to test
                Toast.makeText(HeightActivity.this,"height is: " + totalIN, Toast.LENGTH_SHORT).show();

                //destroy this view and go to main activity
                finish();
            }
        });

    }

}
