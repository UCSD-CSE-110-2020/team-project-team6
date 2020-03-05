package com.example.team_project_team6.notification;

import com.google.firebase.messaging.RemoteMessage;

public interface NotificationService {
    void onMessageReceived(RemoteMessage remoteMessage);
}
