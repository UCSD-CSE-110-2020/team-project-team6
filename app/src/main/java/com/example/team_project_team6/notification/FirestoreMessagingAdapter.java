package com.example.team_project_team6.notification;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.messaging.RemoteMessage;

public class FirestoreMessagingAdapter implements NotificationService {
    private final String TAG = FirestoreMessagingAdapter.class.getSimpleName();
    private static NotificationService instance;

    private static final String COLLECTION_KEY = "chats";
    private static final String DOCUMENT_KEY = "chat1";
    private static final String FROM_KEY = "from";
    private static final String TEXT_KEY = "text";
    private CollectionReference team_collection;

    public FirestoreMessagingAdapter(CollectionReference team_collection) {
        this.team_collection = team_collection;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

    }
}
