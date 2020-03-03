package com.example.team_project_team6.fitness;

import android.util.Log;

import com.example.team_project_team6.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;

public class TestAdapter implements FitnessService {
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "TestAdapter";
    private long mSteps;

    private MainActivity activity;

    public TestAdapter(MainActivity activity) {
        this.activity = activity;
        this.mSteps = 0;
    }

    public void mock_steps(long steps) {
        mSteps = steps;
    }

    public void setup() {
        // Empty
    }

    public void updateStepCount() {
        mSteps += 5;
        activity.setStepCount(mSteps);
    }

    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }

//    @Override
//    public String getUserEmail() {
//        return null;
//    }
}
