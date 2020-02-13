package com.example.team_project_team6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.team_project_team6.fitness.FitnessService;
import com.example.team_project_team6.fitness.FitnessServiceFactory;
import com.example.team_project_team6.fitness.GoogleFitAdapter;
import com.example.team_project_team6.fitness.TestAdapter;
import com.example.team_project_team6.ui.home.HomeViewModel;
import com.example.team_project_team6.ui.walk.WalkViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "MainActivity";
    private static final String GOOGLE_FITNESS_KEY = "GOOGLE_FIT";
    private static final String MOCK_FITNESS_KEY = "TEST_FIT";
    private String fitnessServiceKey = GOOGLE_FITNESS_KEY;

    private FitnessService fitnessService;

    private HomeViewModel homeViewModel;
    private WalkViewModel walkViewModel;
    private StopWatch sw;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw = new StopWatch();

        // launch height/permission activity if user height hasn't been saved (first-time user)
        SharedPreferences spfs = this.getSharedPreferences("user_data", MODE_PRIVATE);
        if (!spfs.contains("user_height")) {
            launchPermissionActivity();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_walk, R.id.navigation_routes)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FitnessServiceFactory.put(GOOGLE_FITNESS_KEY, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });

        FitnessServiceFactory.put(MOCK_FITNESS_KEY, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestAdapter(mainActivity);
            }
        });

        //setFitnessServiceKey(getIntent().getStringExtra(FITNESS_SERVICE_KEY));
        setFitnessServiceKey(MOCK_FITNESS_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(1000); // update once a second
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }


    public void launchPermissionActivity(){
        Intent intent = new Intent(this, PermissonActivity.class);
        startActivity(intent);
    }

    public void setStepCount(long stepCount) {
        homeViewModel.updateDailySteps(stepCount);
        //walkViewModel.updateWalkSteps(stepCount);
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    private class AsyncTaskRunner extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                while (true) {
                    Thread.sleep(params[0]);
                    fitnessService.updateStepCount();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public void runStopWatch (){
        sw.runStopWatch(walkViewModel);
    }

    public void stopWatch(){
        sw.stopWatch();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
