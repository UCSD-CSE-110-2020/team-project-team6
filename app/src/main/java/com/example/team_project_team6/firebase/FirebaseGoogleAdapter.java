package com.example.team_project_team6.firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseGoogleAdapter implements IFirebase {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Gson gson;

    private String TIMESTAMP_KEY = "timestamp";

    public FirebaseGoogleAdapter() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = null;
        gson = new Gson();
    }

    @Override
    public void authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(authCredential).addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Signed in with Google to Firebase");
                    user = auth.getCurrentUser();

                    Toast.makeText(activity, "Signed into Firebase successfully", Toast.LENGTH_LONG).show();
                    Toast.makeText(activity, "Logged in with email: " + getEmail(), Toast.LENGTH_LONG).show();

                    // Create the user document if it does not exist (first time user?)
                    // Adds the user to a random team by default
                    db.collection("users")
                            .document(getEmail())
                            .get()
                            .addOnCompleteListener(createTask -> {
                        if (createTask.isSuccessful()) {
                            DocumentSnapshot document = createTask.getResult();
                            if (!Objects.requireNonNull(document).exists()) {
                                Map<String, String> team = new HashMap<>();
                                String uuid = UUID.randomUUID().toString();
                                team.put("team", uuid);

                                Log.d(TAG, "No team found, assuming new user. Creating team " + uuid + " for user " + getEmail());

                                Log.d(TAG, "Creating user " + getEmail());
                                db.collection("users")
                                        .document(getEmail())
                                        .set(team);

                                Map<String, List<TeamMember>> uuidTeam = new HashMap<>();
                                List<TeamMember> members = new ArrayList<>();

                                String[] name = getName().split(" ");
                                TeamMember member = new TeamMember();
                                member.setEmail(getEmail());
                                member.setFirstName(name[0]);
                                member.setLastName(name[1]);

                                members.add(member);
                                uuidTeam.put(uuid, members);

                                Log.d(TAG, "Creating team " + uuid);
                                db.collection("teams")
                                        .document(uuid)
                                        .set(uuidTeam);
                            } else {
                                Log.d(TAG, "Found user " + getEmail());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to sign in to Firebase");
                    Toast.makeText(activity, "ERROR: Failed to sign into Firebase", Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public Boolean getSignedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public String getEmail() {
        if (user == null) {
            Log.e(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getEmail();
        }
    }

    @Override
    public String getName() {
        if (user == null) {
            Log.e(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getDisplayName();
        }
    }

    @Override
    public String getId() {
        if (user == null) {
            Log.e(TAG, "You must sign in before calling this method");
            return "ERR_NOT_SIGNED_IN";
        } else {
            return user.getUid();
        }
    }

    @Override
    public String getTeam() {
        String[] teamUUID = new String[1];
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null && doc.exists()) {
                            teamUUID[0] = (String) doc.get("team");
                            Log.d(TAG, "Successfully retrieved team info");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.e(TAG, "Failed to retrieve team UUID");
                    }
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));
        return teamUUID[0];
    }

    @Override
    public void uploadRouteData(Route route) {
        if (user == null) {
            Log.d(TAG, "Could not upload route data without signing in");
            return;
        }

        Map<String, String> jsonToMap = gson.fromJson(
                gson.toJson(route), new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        Log.d(TAG, "save with: " + getEmail());
        DocumentReference uidRef = db.collection("users").document(getEmail());
        uidRef.collection("routes")
                .document(route.getName())
                .set(jsonToMap)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Route saved to routes/" + route.getName() + "/");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error adding document", e);
                });
    }

    @Override
    public synchronized LiveData<ArrayList<Route>> downloadRouteData() {
        MutableLiveData<ArrayList<Route>> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not download route data without signing in");
            return data;
        }

        db.collection("users")
                .document(getEmail())
                .collection("routes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "Successfully read data but data was empty");
                    } else {
                        ArrayList<Route> list = new ArrayList<>();

                        List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshots) {
                            Map<String, Object> map = snapshot.getData();
                            Route route = gson.fromJson(gson.toJson(map), Route.class);
                            list.add(route);
                        }

                        Log.i(TAG, "Successfully read route data");

                        data.postValue(list);
                    }
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));

        return data;
    }

    public void uploadTeamRequest(String email) {
        if (user == null) {
            Log.d(TAG, "Could not send team request without signing in");
            return;
        }

        Map<String, Object> requestFrom = new HashMap<>();
        requestFrom.put("invitationFrom", getEmail());

        // to user I am sending request to, update the "invitationFrom" field
        db.collection("users")
                .document(email)
                .update(requestFrom)
                .addOnSuccessListener(documentReference -> {

                    // retrieve my team uuid
                    db.collection("users")
                            .document(getEmail())
                            .get()
                            .addOnCompleteListener(getMyTeamTask -> {
                                if (getMyTeamTask.isSuccessful()) {
                                    DocumentSnapshot teamUuidDoc = getMyTeamTask.getResult();
                                    if (teamUuidDoc != null) {
                                        String teamUUID = (String) teamUuidDoc.get("team");

                                        // get recipient's team uuid
                                        db.collection("users")
                                                .document(email)
                                                .get()
                                                .addOnCompleteListener(getTheirTeamTask -> {
                                                    if (getTheirTeamTask.isSuccessful()) {
                                                        DocumentSnapshot recipientTeamUuidDoc = getTheirTeamTask.getResult();
                                                        if (recipientTeamUuidDoc != null) {
                                                            String recipientUUID = (String) recipientTeamUuidDoc.get("team");

                                                            // get recipient's member info from their team
                                                            db.collection("users")

                                                        } else {
                                                            Log.d(TAG, "No such document");
                                                        }
                                                    } else {
                                                        Log.e(TAG, "Failed to retrieve recipient's team uuid");
                                                    }
                                                })
                                                .addOnFailureListener(e -> Log.e(TAG, "Error retrieving recipient's team uuid.", e))

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.e(TAG, "Failed to retrieve team uuid");
                                }

                                TeamMember requestTo = new TeamMember();


                                Log.d(TAG, "Team request sent to user with email: " + email);
                                db.collection("users")
                                        .document(getEmail())
                                        .update(requestTo)
                                        .addOnSuccessListener(d -> Log.d(TAG, "Pending invite info sent to user with email: " + getEmail()))
                                        .addOnFailureListener(e -> Log.e(TAG, "Error sending pending invite info to user with email: " + getEmail(), e));


                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Error retrieving user's team uuid.", e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error sending invite to user with email: " + email, e));
    }

    public synchronized LiveData<HashMap<String, String>> downloadTeamRequest() {
        MutableLiveData<HashMap<String, String>> data = new MutableLiveData<>();

        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getInviteTask -> {
                    if (getInviteTask.isSuccessful()) {
                        DocumentSnapshot inviteDoc = getInviteTask.getResult();

                        if (inviteDoc != null && inviteDoc.exists()) {
                            if (inviteDoc.contains("invitationFrom")) {
                                String inviteFrom = (String) inviteDoc.get("invitationFrom");
                                HashMap<String, String> res = new HashMap<>();
                                res.put("type", "invitationFrom");
                                res.put("email", inviteFrom);

                                data.postValue(res);

                            } else if (inviteDoc.contains("invitationTo")) {
                                String inviteTo = (String) inviteDoc.get("invitationTo");
                                HashMap<String, String> res = new HashMap<>();
                                res.put("type", "invitationTo");
                                res.put("email", inviteTo);

                                data.postValue(res);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.e(TAG, "Failed to retrieve team invitations");
                    }
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));;

        return data;
    }

    public synchronized LiveData<ArrayList<TeamMember>> downloadTeamData() {
        MutableLiveData<ArrayList<TeamMember>> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not download route data without signing in");
            return data;
        }

        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getTeamNameTask -> {
                    if (getTeamNameTask.isSuccessful()) {
                        DocumentSnapshot teamNameDoc = getTeamNameTask.getResult();
                        if (teamNameDoc != null && teamNameDoc.exists()) {
                            String teamUUID = (String) teamNameDoc.get("team");

                            // go to the teams collection outside of users and get the list of emails
                            db.collection("teams")
                                    .document(teamUUID)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot doc = task.getResult();
                                            if (doc != null && doc.exists()) {
                                                ArrayList<HashMap<String, String>> memMaps = (ArrayList<HashMap<String, String>>) doc.get(teamUUID);
                                                ArrayList<TeamMember> members = new ArrayList<>();
                                                for (HashMap<String, String> m : memMaps) {
                                                    members.add(new TeamMember(m));
                                                }
                                                data.postValue(members);
                                                Log.d(TAG, "Successfully retrieved team members");
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.e(TAG, "Failed to retrieve team members");
                                        }
                                    })
                                    .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.e(TAG, "Failed to retrieve team UUID");
                    }
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));

        return data;
    }
}