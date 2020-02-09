package com.example.team_project_team6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PermissonActivity extends AppCompatActivity {

    private Button btContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisson);

        btContinue = findViewById(R.id.btContinue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchHeightActivity();
                finish();
            }
        });
    }

    public void launchHeightActivity(){
        Intent intent = new Intent(this, HeightActivity.class);
        startActivity(intent);
    }
}
