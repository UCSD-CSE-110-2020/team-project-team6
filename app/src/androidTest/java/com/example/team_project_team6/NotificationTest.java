package com.example.team_project_team6;

import android.app.Activity;

import com.example.team_project_team6.firebase.IFirebase;
import com.example.team_project_team6.model.ProposedWalk;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.model.TeamMessage;
import com.example.team_project_team6.notification.INotification;
import com.example.team_project_team6.ui.team.TeamViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;


public class NotificationTest {
    class MockFirebaseAdapter implements IFirebase {

        @Override
        public void authenticateWithGoogle(@NonNull Activity activity, @NonNull GoogleSignInAccount account) {

        }

        @Override
        public Boolean getSignedIn() {
            return null;
        }

        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public LiveData<String> getTeamUUID() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public void uploadRouteData(Route route) {

        }

        @Override
        public LiveData<ArrayList<Route>> downloadRouteData(String email) {
            return null;
        }

        @Override
        public void acceptTeamRequest() {

        }

        @Override
        public void declineTeamRequest() {

        }

        @Override
        public void uploadTeamRequest(String member) {

        }

        @Override
        public LiveData<HashMap<String, String>> downloadTeamRequest() {
            return null;
        }

        @Override
        public LiveData<ArrayList<TeamMember>> downloadTeamData() {
            return null;
        }

        @Override
        public LiveData<HashMap<String, String>> downloadMemberGoingStatuses() {
            return null;
        }

        @Override
        public void uploadMemberGoingStatus(String attendance) {

        }

        @Override
        public void uploadProposedWalk(ProposedWalk proposedWalk) {

        }

        @Override
        public LiveData<ProposedWalk> downloadProposedWalk() {
            return null;
        }

        @Override
        public void setNotificationAdapter(INotification notificationAdapter) {

        }

        @Override
        public void sendTeamNotification(TeamMessage message, boolean isMessageForProposeWalk) {
            notificationWasSent = true;
        }
    }

    boolean notificationWasSent;

    @Test
    public void testSendNotification(){

        IFirebase firebaseAdapter = new MockFirebaseAdapter();
        SaveData saveData = new SaveData(InstrumentationRegistry.getInstrumentation().getTargetContext(), firebaseAdapter);
        TeamViewModel testModel = new TeamViewModel();
        testModel.setSaveData(saveData);
        TeamMessage message = new TeamMessage("test@notification", "hello");
        testModel.sendTeamNotification(message, true);
        assertEquals(notificationWasSent, true);
    }
}