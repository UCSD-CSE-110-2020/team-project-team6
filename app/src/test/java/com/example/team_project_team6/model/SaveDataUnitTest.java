package com.example.team_project_team6.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

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
        saveData = new SaveData(context);

        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(spfs);
        Mockito.when(context.getSharedPreferences(anyString(), anyInt()).edit()).thenReturn(editor);
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
}