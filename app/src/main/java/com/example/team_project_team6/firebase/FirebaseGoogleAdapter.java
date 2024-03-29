package com.example.team_project_team6.firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.team_project_team6.model.ProposedWalk;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamInvite;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.model.TeamMessage;
import com.example.team_project_team6.notification.INotification;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private MutableLiveData<String> teamUUID;
    private INotification notificationAdapter;

    public FirebaseGoogleAdapter() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = null;
        teamUUID = new MutableLiveData<>();
        gson = new Gson();

    }
    @Override
    public void setNotificationAdapter(INotification notificationAdapter){
        this.notificationAdapter = notificationAdapter;
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
                                Map<String, String> userInfo = new HashMap<>();
                                String uuid = UUID.randomUUID().toString();
                                String[] name = getName().split(" ");

                                userInfo.put("team", uuid);
                                userInfo.put("firstName", name[0]);
                                userInfo.put("lastName", name[1]);
                                userInfo.put("token_id", FirebaseInstanceId.getInstance().getToken());

                                Log.d(TAG, "No team found, assuming new user. Creating team " + uuid + " for user " + getEmail());

                                Log.d(TAG, "Creating user " + getEmail());
                                db.collection("users")
                                        .document(getEmail())
                                        .set(userInfo);

                                Map<String, List<TeamMember>> uuidTeam = new HashMap<>();
                                List<TeamMember> members = new ArrayList<>();

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

                                //register a team notification
                                notificationAdapter.subscribeToTeamTopic(uuid);

                            } else {
                                Log.d(TAG, "Found user " + getEmail());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to sign in to Firebase");
                    Toast.makeText(activity, "ERROR: Failed to sign into Firebase", Toast.LENGTH_LONG).show();
                }

                //update token ID when user logout or uninstall the app
                Map<String, Object> token_id = new HashMap<>();
                token_id.put("token_id", FirebaseInstanceId.getInstance().getToken());
                db.collection("users").document(user.getEmail()).update(token_id)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Token ID successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating Token ID", e);
                            }
                        });
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
    public LiveData<String> getTeamUUID() {
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d(TAG, "Successfully retrieved team UUID");

                    String uuid = documentSnapshot.getString("team");
                    teamUUID.postValue(uuid);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to retrieve team UUID");
                });

        return teamUUID;
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
    public synchronized LiveData<ArrayList<Route>> downloadRouteData(String email) {
        MutableLiveData<ArrayList<Route>> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not download route data without signing in");
            return data;
        }

        db.collection("users")
                .document(email)
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

    public void acceptTeamRequest() {
        if (user == null) {
            Log.d(TAG, "Could not send team request without signing in");
            return;
        }

        // get sender's email to update the sender's team and invitation status
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getReceiverTask -> {
                    if (getReceiverTask.isSuccessful()) {
                        DocumentSnapshot receiverDoc = getReceiverTask.getResult();
                        if (receiverDoc != null) {
                            String senderEmail = (String) ((HashMap<String, Object>) receiverDoc.get("invitation")).get("email");
                            String oldTeam = (String) receiverDoc.get("team"); // go in here to erase receiver's data from their team
                            String[] name = getName().split(" ");
                            TeamMember receiver = new TeamMember(getEmail(), name[0], name[1]);

                            // get the sender's team from their email
                            db.collection("users")
                                    .document(senderEmail)
                                    .get()
                                    .addOnCompleteListener(getTeamTask -> {
                                        if (getTeamTask.isSuccessful()) {
                                            DocumentSnapshot newTeamDoc = getTeamTask.getResult();
                                            if (newTeamDoc != null) {
                                                String newTeam = (String) newTeamDoc.get("team");

                                                // add receiver to sender's team
                                                db.collection("teams")
                                                        .document(newTeam)
                                                        .update(newTeam, FieldValue.arrayUnion(receiver))
                                                        .addOnSuccessListener(d -> Log.d(TAG, "Added " + getEmail() + " to " + senderEmail + "'s team."))
                                                        .addOnFailureListener(e -> Log.e(TAG, "Error adding user to new team", e));

                                                // remove receiver from their old team
                                                db.collection("teams")
                                                        .document(oldTeam)
                                                        .update(oldTeam, FieldValue.arrayRemove(receiver))
                                                        .addOnSuccessListener(d -> Log.d(TAG, "Removed " + getEmail() + " from old team."))
                                                        .addOnFailureListener(e -> Log.e(TAG, "Error removing user from old team", e));

                                                HashMap<String, Object> updatedTeam = new HashMap<>();
                                                updatedTeam.put("team", newTeam);

                                                // save the new team uuid to the user's fields
                                                db.collection("users")
                                                        .document(getEmail())
                                                        .update(updatedTeam)
                                                        .addOnSuccessListener(d -> Log.d(TAG, "Updated user's team."))
                                                        .addOnFailureListener(e -> Log.e(TAG, "Error updating user's team", e));

                                                //add member to subscribe team notification
                                                notificationAdapter.subscribeToTeamTopic(newTeam);

                                                // delete the sender's invitation receipt
                                                db.collection("users")
                                                        .document(senderEmail)
                                                        .update("invitation", FieldValue.delete())
                                                        .addOnSuccessListener(d -> Log.d(TAG, "Removed invitation item from sender's fields."))
                                                        .addOnFailureListener(e -> Log.e(TAG, "Error removing sender's invitation receipt", e));

                                                // delete the receiver's invitation receipt
                                                db.collection("users")
                                                        .document(getEmail())
                                                        .update("invitation", FieldValue.delete())
                                                        .addOnSuccessListener(d -> Log.d(TAG, "Removed invitation to join team."))
                                                        .addOnFailureListener(e -> Log.e(TAG, "Error removing received invitation.", e));

                                            } else {
                                                Log.e(TAG, "Document does not exist.");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to get sender's team from email", e));

                        } else {
                            Log.e(TAG, "Document does not exist.");
                        }

                    } else {
                        Log.e(TAG, "Document does not exist.");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to get sender's email", e));
    }

    public void declineTeamRequest() {
        if (user == null) {
            Log.d(TAG, "Could not send team request without signing in");
            return;
        }

        // get sender's email
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getSenderTask -> {

                    if (getSenderTask.isSuccessful()) {
                        DocumentSnapshot senderDoc = getSenderTask.getResult();
                        if (senderDoc != null) {
                            String senderEmail = (String) ((HashMap<String, Object>) senderDoc.get("invitation")).get("email");

                            // delete the sender's invitation receipt
                            db.collection("users")
                                    .document(senderEmail)
                                    .update("invitation", FieldValue.delete())
                                    .addOnSuccessListener(d -> Log.d(TAG, "Removed invitation item from sender's fields."))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error removing sender's invitation receipt", e));

                            // delete the receiver's invitation receipt
                            db.collection("users")
                                    .document(getEmail())
                                    .update("invitation", FieldValue.delete())
                                    .addOnSuccessListener(d -> Log.d(TAG, "Removed invitation to join team."))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error removing received invitation.", e));

                        } else {
                            Log.e(TAG, "Document does not exist.");
                        }

                    } else {
                        Log.e(TAG, "Document does not exist.");
                    }

                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to get sender's email", e));
    }

    public void uploadTeamRequest(String email) {
        if (user == null) {
            Log.d(TAG, "Could not send team request without signing in");
            return;
        }

        Map<String, Object> requestFrom = new HashMap<>();
        TeamInvite from = new TeamInvite();

        String message = "You received an invitation from ";
        if(getName() != null) {
            message += getName();
        }else{
            message += email;
        }

        from.setEmail(getEmail());
        from.setName(getName());
        from.setMessage(message);
        from.setToOrFrom("from");
        from.setTeamUUID("do we need this?");

        requestFrom.put("invitation", from);

        // to user I am sending request to, update the "invitationFrom" field
        db.collection("users")
                .document(email)
                .update(requestFrom)
                .addOnSuccessListener(documentReference -> {

                    // for the user sending the request, save a receipt of who the invitation was sent to
                    db.collection("users")
                            .document(email)
                            .get()
                            .addOnCompleteListener(getRecipientTask -> {
                                if (getRecipientTask.isSuccessful()) {
                                    DocumentSnapshot recipientDoc = getRecipientTask.getResult();
                                    if (recipientDoc != null) {
                                        Map<String, Object> requestTo = new HashMap<>();
                                        TeamInvite to = new TeamInvite();
                                        to.setEmail(email);
                                        to.setName((String) recipientDoc.get("firstName") + " " +
                                                (String) recipientDoc.get("lastName"));
                                        to.setToOrFrom("to");

                                        requestTo.put("invitation", to);

                                        Log.d(TAG, "Team request sent to user with email: " + email);
                                        db.collection("users")
                                                .document(getEmail())
                                                .update(requestTo)
                                                .addOnSuccessListener(d -> Log.d(TAG, "Pending invite info sent to user with email: " + getEmail()))
                                                .addOnFailureListener(e -> Log.e(TAG, "Error sending pending invite info to user with email: " + getEmail(), e));
                                    }
                                }

                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Error getting name of user with email: " + email, e));

                })
                .addOnFailureListener(e -> Log.e(TAG, "Error sending invite to user with email: " + email, e));
    }

    public synchronized LiveData<HashMap<String, String>> downloadTeamRequest() {
        MutableLiveData<HashMap<String, String>> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not send team request without signing in");
            return data;
        }

        // retrieve team invitation
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getInviteTask -> {
                    if (getInviteTask.isSuccessful()) {
                        DocumentSnapshot inviteDoc = getInviteTask.getResult();

                        if (inviteDoc != null && inviteDoc.exists()) {

                            // post invitation if user has one
                            if (inviteDoc.contains("invitation")) {
                                HashMap<String, String> invite = (HashMap<String, String>) inviteDoc.get("invitation");
                                data.postValue(invite);
                            } else {
                                Log.i(TAG, "couldn't find invitation field");
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.e(TAG, "Failed to retrieve team invitations");
                    }
                })
                .addOnFailureListener(queryDocumentSnapshots -> Log.e(TAG, "Failed to read data from firebase"));

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

    public void uploadProposedWalk(ProposedWalk proposedWalk) {
        if(user == null) {
            Log.d(TAG, "Could not send proposed walk without signing in");
            return;
        }

        // retrieve team uuid
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getTeamTask -> {
                    if (getTeamTask.isSuccessful()) {
                        DocumentSnapshot teamDoc = getTeamTask.getResult();
                        if (teamDoc != null) {

                            String team = (String) teamDoc.get("team");

                            // convert proposed walk object to a hash map to save inside firebase
                            HashMap<String, Object> pwMap = new HashMap<>();
                            proposedWalk.setProposer(getEmail());
                            Map<String, Object> jsonToMap = gson.fromJson(
                                    gson.toJson(proposedWalk), new TypeToken<HashMap<String, Object>>() {}.getType()
                            );

                            // create map to save walk proposer's response status (assumed going to walk)
                            HashMap<String, Object> attendanceMap = new HashMap<>();
                            attendanceMap.put(getName(), "accepted");

                            pwMap.put("proposedWalk", jsonToMap);
                            pwMap.put("attendance", attendanceMap);

                            // save proposed walk to team document
                            db.collection("teams")
                                    .document(team)
                                    .update(pwMap)
                                    .addOnCompleteListener(d -> Log.d(TAG, "Successfully stored proposed walk in team database"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to update proposed walk", e));

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to read data from firebase", e));
    }

    @Override
    public void deleteProposedWalk() {
        if(user == null) {
            Log.d(TAG, "Could not delete proposed walk without signing in");
            return;
        }

        // retrieve team uuid
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getTeamTask -> {
                    if (getTeamTask.isSuccessful()) {
                        DocumentSnapshot teamDoc = getTeamTask.getResult();
                        if (teamDoc != null) {
                            String team = teamDoc.getString("team");

                            Map<String,Object> updates = new HashMap<>();
                            updates.put("attendance", FieldValue.delete());
                            updates.put("proposedWalk", FieldValue.delete());

                            // delete proposed walk from the team document
                            db.collection("teams")
                                    .document(team)
                                    .update(updates)
                                    .addOnSuccessListener(d -> Log.d(TAG, "Successfully deleted proposed walk in team database"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to delete proposed walk", e));

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to read data from firebase", e));
    }

    public synchronized LiveData<ProposedWalk> downloadProposedWalk() {
        MutableLiveData<ProposedWalk> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not download member going statuses data without signing in");
            return data;
        }

        // retrieve team uuid
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(getTeamTask -> {
                    if (getTeamTask.isSuccessful()) {
                        DocumentSnapshot teamDoc = getTeamTask.getResult();
                        if (teamDoc != null) {

                            String team = (String) teamDoc.get("team");

                            // retrieve proposed walk
                            db.collection("teams")
                                    .document(team)
                                    .get()
                                    .addOnCompleteListener(getWalkTask -> {
                                        if (getWalkTask.isSuccessful()) {
                                            DocumentSnapshot pWalkDoc = getWalkTask.getResult();
                                            if (pWalkDoc != null && pWalkDoc.exists()) {
                                                Map<String, Object> map = pWalkDoc.getData();
                                                Map<String, Object> pWalkMap = (Map<String, Object>) map.get("proposedWalk");
                                                ProposedWalk pWalk = gson.fromJson(gson.toJson(pWalkMap), ProposedWalk.class);

                                                // if a walk has been proposed, then identify if current user was the proposer
                                                // and add the walk to the mutablelivedata object
                                                if (pWalk != null) {
                                                    if (pWalk.getProposer().equals(getEmail())) {
                                                        Log.i(TAG, "Setting current user as proposer of proposed walk");
                                                        pWalk.setProposer("yes");
                                                    } else {
                                                        Log.i(TAG, "Setting current user as receiver of proposed walk");
                                                        pWalk.setProposer("no");
                                                    }
                                                    data.postValue(pWalk);
                                                } else {
                                                    Log.i(TAG, "There are no proposed walks in Firebase.");
                                                }

                                            } else {
                                                Log.d(TAG, "Failed to retrieve proposed walk from teams");
                                            }
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to download proposed walk", e));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to read data from firebase", e));


        return data;
    }

    public void uploadMemberGoingStatus(String attendance) {
        if (user == null) {
            Log.d(TAG, "Could not download member going statuses data without signing in");
            return;
        }

        // get team uuid
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(teamTask -> {
                    if (teamTask.isSuccessful()) {
                        DocumentSnapshot teamDoc = teamTask.getResult();
                        if (teamDoc != null) {
                            String team = (String) teamDoc.get("team");

                            // get list of team members who have already responded to the proposed walk
                            db.collection("teams")
                                    .document(team)
                                    .get()
                                    .addOnCompleteListener(getAttendanceTask -> {
                                        if (getAttendanceTask.isSuccessful()) {
                                            DocumentSnapshot attendDoc = getAttendanceTask.getResult();
                                            if (attendDoc != null) {
                                                HashMap<String, Object> map = new HashMap<>();
                                                HashMap<String, Object> attendMap = (HashMap<String, Object>) attendDoc.get("attendance");
                                                attendMap.put(getName(), attendance);
                                                map.put("attendance", attendMap);

                                                // update proposed walk responses with current user's response
                                                db.collection("teams")
                                                    .document(team)
                                                    .update(map)
                                                    .addOnCompleteListener(d -> Log.d(TAG, "Successfully updated proposed walk attendance"))
                                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to update proposed walk attendance", e));

                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to read attendance data from firebase", e));

                        } else {
                            Log.d(TAG, "Task to retrieve team information failed.");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to read user's team from firebase", e));
    }

    public synchronized LiveData<HashMap<String, String>> downloadMemberGoingStatuses() {
        MutableLiveData<HashMap<String, String>> data = new MutableLiveData<>();

        if (user == null) {
            Log.d(TAG, "Could not download member going statuses data without signing in");
            return data;
        }

        // get team uuid
        db.collection("users")
                .document(getEmail())
                .get()
                .addOnCompleteListener(teamTask -> {
                    if (teamTask.isSuccessful()) {
                        DocumentSnapshot teamDoc = teamTask.getResult();
                        if (teamDoc != null) {
                            String team = (String) teamDoc.get("team");

                            // retrieve responses to the proposed walk
                            db.collection("teams")
                                    .document(team)
                                    .get()
                                    .addOnCompleteListener(getAttendanceTask -> {
                                        if (getAttendanceTask.isSuccessful()) {
                                            DocumentSnapshot attendDoc = getAttendanceTask.getResult();
                                            if (attendDoc != null) {
                                                HashMap<String, String> attendMap = (HashMap<String, String>) attendDoc.get("attendance");
                                                if (attendMap != null ) {
                                                    String self = attendMap.get(getName());
                                                    if (self != null) {
                                                        attendMap.remove(getName());
                                                        attendMap.put("self", self);
                                                    }
                                                    data.postValue(attendMap);
                                                }
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to read attendance data from firebase", e));

                        } else {
                            Log.d(TAG, "Task to retrieve team information failed.");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to read user's team from firebase", e));

        return data;
    }

    public void sendTeamNotification(TeamMessage message, boolean isMessageForProposeWalk){
        if (user == null) {
            Log.d(TAG, "Could not send message data without signing in");
            return;
        }


        db.collection("users").document(getEmail()).get().addOnCompleteListener(getSenderEmail -> {
            if(getSenderEmail.isSuccessful()){
                DocumentSnapshot snapshot = getSenderEmail.getResult();

                //get team ID as a topic_key base on email
                if(isMessageForProposeWalk){
                    //if user is already in team, just get team ID base on that user
                    String teamID = snapshot.getString("team");
                    Log.i(TAG, "get team ID as topic key: " + teamID);
                    notificationAdapter.sendTeamNotification(teamID, message);
                }else {
                    //if user is going to accept, get teamID from sender email
                    String senderEmail = snapshot.getString("invitation.email");

                    db.collection("users").document(senderEmail).get().addOnCompleteListener(getSenderTeamID -> {
                        if(getSenderTeamID.isSuccessful()){
                            String topic_key = getSenderTeamID.getResult().getString("team");
                            Log.i(TAG, "get sender team ID as topic key: " + topic_key);

                            //send topic id and notification to FirebaseMessagingAdapter
                            notificationAdapter.sendTeamNotification(topic_key, message);

                        }else {
                            Log.e(TAG, "Failed to retrieve sender's team UUID!");
                        }

                    });

                }
            } else {
                Log.e(TAG, "Failed to retrieve sender email!");
            }
        });
    }

}
