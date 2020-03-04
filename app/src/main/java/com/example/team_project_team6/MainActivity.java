package com.example.team_project_team6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.team_project_team6.firebase.FirebaseGoogleAdapter;
import com.example.team_project_team6.fitness.FitnessService;
import com.example.team_project_team6.fitness.FitnessServiceFactory;
import com.example.team_project_team6.fitness.GoogleFitAdapter;
import com.example.team_project_team6.fitness.TestAdapter;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.ui.home.HomeViewModel;
import com.example.team_project_team6.ui.new_route.NewRouteViewModel;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.example.team_project_team6.ui.routes.RoutesViewModel;
import com.example.team_project_team6.ui.team.TeamViewModel;
import com.example.team_project_team6.ui.walk.WalkViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "MainActivity";
    private static final String GOOGLE_FITNESS_KEY = "GOOGLE_FIT";
    public static final String MOCK_FITNESS_KEY = "TEST_FIT";
    private String fitnessServiceKey = GOOGLE_FITNESS_KEY;

    private FitnessService fitnessService;
    private FirebaseGoogleAdapter fgadapter;

    private HomeViewModel homeViewModel;
    private WalkViewModel walkViewModel;
    private Long walkStartingStep;

    private AppBarConfiguration appBarConfiguration;

    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;

    private AsyncTaskRunner runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // launch height/permission activity if user height hasn't been saved (first-time user)
        SharedPreferences spfs = this.getSharedPreferences("user_data", MODE_PRIVATE);
        if (!spfs.contains("user_height")) {
            launchPermissionActivity();
        }

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_walk, R.id.navigation_routes, R.id.navigation_team)
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

        if (getIntent().getStringExtra(FITNESS_SERVICE_KEY) != null) {
            setFitnessServiceKey(getIntent().getStringExtra(FITNESS_SERVICE_KEY));
        } else {
            setFitnessServiceKey(GOOGLE_FITNESS_KEY);
        }

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        runner = new AsyncTaskRunner();
        runner.execute(1000); // update once a second

        // Create a firebase adapter and inject it into anything that requires it
        fgadapter = new FirebaseGoogleAdapter();
        SaveData saveData = new SaveData(getApplicationContext(), fgadapter);

        // Inject into walkViewModel
        walkViewModel.setSaveData(saveData);

        // Inject into NewRouteViewModel
        NewRouteViewModel newRouteViewModel = new ViewModelProvider(this).get(NewRouteViewModel.class);
        newRouteViewModel.setSaveData(saveData);

        // Inject into RoutesViewModel
        RoutesViewModel routesViewModel = new ViewModelProvider(this).get(RoutesViewModel.class);
        routesViewModel.setSaveData(saveData);

        // Inject into homeViewModel
        homeViewModel.setSaveData(saveData);

        // Inject into RouteDetailsViewModel
        RouteDetailsViewModel routeDetailsViewModel = new ViewModelProvider(this).get(RouteDetailsViewModel.class);
        routeDetailsViewModel.setSaveData(saveData);

        // Inject teamViewModel
        TeamViewModel teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        teamViewModel.setFbaseAdapter(fgadapter);

        // Request username and ID tokens for Firebase auth when signing in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build the google sign in client with the specified options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Sign into Firebase with a Google account. If already signed in, then logs into firebase directly.
        signInWithGoogle();
    }

    public void stopAsyncTaskRunner() {
        runner.cancel(true);
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

        // Signed with with Google for Firebase
        if(requestCode == RC_SIGN_IN){
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    GoogleSignInAccount account = task.getResult();

                    if (account != null) {
                        fgadapter.authenticateWithGoogle(this, account);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "ERROR: Signing in failed, try again", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(MainActivity.this, "ERROR: Failed to log into Google", Toast.LENGTH_LONG).show();
            });
        }
    }

    public void signInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void launchPermissionActivity(){
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
    }

    public void setStepCount(long stepCount) {
        homeViewModel.updateDailySteps(stepCount); // update step count on home screen

        // update steps moved just on current walk if the user is currently on a walk
        if (walkViewModel.isWalking()) {
            if (walkStartingStep == null) {
                walkStartingStep = stepCount;
            }

            walkViewModel.updateWalkSteps(stepCount - walkStartingStep);
        } else {
            walkStartingStep = null;
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}