package com.example.team_project_team6.notification;

import com.example.team_project_team6.model.TeamMessage;

public interface INotification {
    void subscribeToTeamTopic(String topic_id);
    void sendTeamNotification(String topic_id, TeamMessage message);
    void unsubscribeFromTeamTopic(String topic_id);
}
