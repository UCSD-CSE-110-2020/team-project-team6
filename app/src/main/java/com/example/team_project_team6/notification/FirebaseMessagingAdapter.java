package com.example.team_project_team6.notification;

import android.util.Log;

import com.example.team_project_team6.model.TeamMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class FirebaseMessagingAdapter implements INotification {
    private static final String TAG = FirebaseMessagingAdapter.class.getSimpleName();
    private FirebaseFirestore db;

    public FirebaseMessagingAdapter(){
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void subscribeToTeamTopic(String topic_id){
        Log.i(TAG, "Subcribing a topic with team ID: " + topic_id );
        //subscribe a topic
        FirebaseMessaging.getInstance().subscribeToTopic(topic_id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to team notifications with topic, successfully";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe to notifications failed";
                        }
                        Log.i(TAG, msg);
                    }
                });
    }

    @Override
    public void sendTeamNotification(String topic_id, TeamMessage message){

        Gson gson = new Gson();
        Map<String, Object> jsonToMap = gson.fromJson(
                gson.toJson(message), new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        Log.i(TAG, "Push team notification with topic ID " + topic_id + " from "
                + message.getFromEmail() + " - " + message.getMessage());

        db.collection("team_notifications").document(topic_id)
                .collection("messages").add(jsonToMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Team notification written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Team notification", e);
                    }
                });
    }

    @Override
    public void unsubscribeFromTeamTopic(String topic_id){
        Log.i(TAG, "Subcribing a topic with team ID: " + topic_id );
        //subscribe a topic
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic_id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Unsubscribed to team notifications with topic, successfully!";
                        if (!task.isSuccessful()) {
                            msg = "Unsubscribe to notifications failed";
                        }
                        Log.i(TAG, msg);
                    }
                });
    }
}
