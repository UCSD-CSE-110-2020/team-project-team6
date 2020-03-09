package com.example.team_project_team6.notification;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;

public class FirebaseMessagingAdapter implements INotification {
    private static final String TAG = FirebaseMessagingAdapter.class.getSimpleName();
    private FirebaseFirestore db;

    public FirebaseMessagingAdapter(){
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void subcribeToTeamTopic(String topic_id){
        Log.i(TAG, "Subcribing a topic with team ID: " + topic_id );
        //subcribe a topic
        FirebaseMessaging.getInstance().subscribeToTopic(topic_id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to team notifications with topic";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe to notifications failed";
                        }
                        Log.i(TAG, msg);
                    }
                });
    }


}
