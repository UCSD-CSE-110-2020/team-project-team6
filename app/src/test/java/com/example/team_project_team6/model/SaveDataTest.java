package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.team_project_team6.firebase.FirebaseGoogleAdapter;
import com.example.team_project_team6.firebase.IFirebase;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

public class SaveDataTest {

    @Mock private SharedPreferences spfs;
    @Mock private SharedPreferences.Editor editor;
    @Mock private Context context;
    @Mock private IFirebase adapter;
    private SaveData saveData;


    @Before
    public void setUp() {
        this.context = Mockito.mock(Context.class);
        this.spfs = Mockito.mock(SharedPreferences.class);
        this.editor = Mockito.mock(SharedPreferences.Editor.class);
        this.adapter = Mockito.mock(FirebaseGoogleAdapter.class);

        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(spfs);
        Mockito.when(spfs.edit()).thenReturn(editor);
        saveData = new SaveData(context, adapter);
    }

    @Test
    public void getHeightTest() {
        Mockito.when(spfs.getInt(anyString(), anyInt())).thenReturn(65);
        assertEquals(saveData.getHeight(), 65);
    }

    @Test
    public void saveWalkTest() {
        Walk walk = new Walk();
        walk.setDist(20.2);
        walk.setStep(55);
        walk.setDuration("10:45:37");

        Gson gson = new Gson();
        String walkJson = gson.toJson(walk);

        Mockito.when(editor.putString(anyString(), anyString())).thenReturn(editor);
        assertEquals(saveData.saveWalk(walk), walkJson);
    }

    @Test
    public void getWalkTest(){
        Walk walk = new Walk();

        Gson gson = new Gson();
        String walkJson = gson.toJson(walk);

        Mockito.when(spfs.getString(anyString(), anyString())).thenReturn(walkJson);
        assertEquals(walkJson, gson.toJson(saveData.getWalk()));
    }

    @Test
    public void saveRoute() {
        Mockito.doNothing().when(adapter).uploadRouteData(any());

        saveData.saveRoute(new Route());
        Mockito.verify(adapter, Mockito.times(1)).uploadRouteData(any());
    }

    @Test
    public void getAllRoutes() {
        LiveData<ArrayList<Route>> expected = new MutableLiveData<>();

        Mockito.when(adapter.getEmail()).thenReturn("test@ucsd.edu");
        Mockito.when(adapter.downloadRouteData(anyString())).thenReturn(expected);
        LiveData<ArrayList<Route>> actual = saveData.getAllRoutes();
        assertEquals(expected, actual);
    }

    @Test
    public void getRoutesFor() {
        LiveData<ArrayList<Route>> expected = new MutableLiveData<>();

        Mockito.when(adapter.downloadRouteData(anyString())).thenReturn(expected);
        LiveData<ArrayList<Route>> actual = saveData.getRoutesFor("test");
        assertEquals(expected, actual);
    }

    @Test
    public void getAllMembers() {
        LiveData<ArrayList<TeamMember>> expected = new MutableLiveData<>();

        Mockito.when(adapter.downloadTeamData()).thenReturn(expected);
        LiveData<ArrayList<TeamMember>> actual = saveData.getAllMembers();
        assertEquals(expected, actual);
    }

    @Test
    public void getTeam() {
        LiveData<String> expected = new MutableLiveData<>();

        Mockito.when(adapter.getTeamUUID()).thenReturn(expected);
        LiveData<String> actual = saveData.getTeam();
        assertEquals(expected, actual);
    }

    @Test
    public void addTeamMember() {
        Mockito.doNothing().when(adapter).uploadTeamRequest(anyString());

        saveData.addTeamMember("test@ucsd.edu");
        Mockito.verify(adapter, Mockito.times(1)).uploadTeamRequest(any());
    }

    @Test
    public void addProposedWalk() {
        Mockito.doNothing().when(adapter).uploadProposedWalk(any());

        saveData.addProposedWalk(new ProposedWalk());
        Mockito.verify(adapter, Mockito.times(1)).uploadProposedWalk(any());
    }

    @Test
    public void getProposedWalk() {
        LiveData<ProposedWalk> expected = new MutableLiveData<>();

        Mockito.when(adapter.downloadProposedWalk()).thenReturn(expected);
        LiveData<ProposedWalk> actual = saveData.getProposedWalk();
        assertEquals(expected, actual);
    }

    @Test
    public void getTeamInviter() {
        LiveData<HashMap<String, String>> expected = new MutableLiveData<>();

        Mockito.when(adapter.downloadTeamRequest()).thenReturn(expected);
        LiveData<HashMap<String, String>> actual = saveData.getTeamInviter();
        assertEquals(expected, actual);
    }

    @Test
    public void getMemberGoingStatuses() {
        LiveData<HashMap<String, String>> expected = new MutableLiveData<>();

        Mockito.when(adapter.downloadMemberGoingStatuses()).thenReturn(expected);
        LiveData<HashMap<String, String>> actual = saveData.getMemberGoingStatuses();
        assertEquals(expected, actual);
    }

    @Test
    public void acceptTeamRequest() {
        Mockito.doNothing().when(adapter).acceptTeamRequest();

        saveData.acceptTeamRequest();
        Mockito.verify(adapter, Mockito.times(1)).acceptTeamRequest();
    }

    @Test
    public void declineTeamRequest() {
        Mockito.doNothing().when(adapter).declineTeamRequest();

        saveData.declineTeamRequest();
        Mockito.verify(adapter, Mockito.times(1)).declineTeamRequest();
    }

    @Test
    public void getEmail() {
        String expected = "test@ucsd.edu";

        Mockito.when(adapter.getEmail()).thenReturn(expected);
        String actual = saveData.getEmail();
        assertEquals(expected, actual);
    }
}