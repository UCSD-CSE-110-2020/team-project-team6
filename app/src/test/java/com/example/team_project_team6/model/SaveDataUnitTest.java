package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(AndroidJUnit4.class)
public class SaveDataUnitTest {

    @Mock private SharedPreferences spfs;
    @Mock private SharedPreferences.Editor editor;
    @Mock private Context context;
    private SaveData saveData;


    @Before
    public void setUp() {
        this.context = Mockito.mock(Context.class);
        this.spfs = Mockito.mock(SharedPreferences.class);
        this.editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(spfs);
        Mockito.when(spfs.edit()).thenReturn(editor);

        saveData = new SaveData(context);
    }

    @Test
    public void getHeight() {
        Mockito.when(spfs.getInt(anyString(), anyInt())).thenReturn(65);
        assertEquals(saveData.getHeight(), 65);
    }

    @Test
    public void saveWalk() {
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
    public void saveRoute() {
        Route route = new Route();
        Walk walk = new Walk();
        walk.setDist(20.2);
        walk.setStep(55);
        walk.setDuration("10:45:37");
        route.setWalk(walk);
        route.setName("test route");
        route.setFeatures(new Features());
        route.setStartPoint("test-start-point");
        route.setLastStartDate(Calendar.getInstance());

        Gson gson = new Gson();
        String routeJson = gson.toJson(route);

        Mockito.when(editor.putString(anyString(), anyString())).thenReturn(editor);
        assertEquals(saveData.saveRoute(route), routeJson);
    }

    @Test
    public void getRouteNames() {
        Map<String, String> testMap = new HashMap<>();
        Set<String> testSet = new HashSet<>();
//        testSet.add("Sun God Lawn");
//        testSet.add("Mandeville");

        Mockito.when(spfs.getAll()).thenReturn(testMap);
        Mockito.when(spfs.getAll().keySet()).thenReturn(testSet);
        assertEquals(testSet, saveData.getRouteNames());
    }

    @Test
    public void getRoute() {
        Route route =  new Route();
        route.setWalk(new Walk());
        route.setName("test route");
        route.setFeatures(new Features());
        route.setStartPoint("test-start-point");
        route.setLastStartDate(Calendar.getInstance());

        Gson gson = new Gson();
        String routeJson = gson.toJson(route);

        Mockito.when(spfs.getString(anyString(), anyString())).thenReturn(routeJson);
        assertEquals(routeJson, gson.toJson(saveData.getRoute(route.getName())));
    }

    @Test
    public void getWalk(){
        Walk walk = new Walk();

        Gson gson = new Gson();
        String walkJson = gson.toJson(walk);

        Mockito.when(spfs.getString(anyString(), anyString())).thenReturn(walkJson);
        assertEquals(walkJson, gson.toJson(saveData.getWalk()));
    }
}