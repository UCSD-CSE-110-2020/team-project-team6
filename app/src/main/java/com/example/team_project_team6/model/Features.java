package com.example.team_project_team6.model;

import android.util.Log;

import java.util.Locale;

public class Features {
    private int level;
    private int directionType;
    private int terrain;
    private boolean isFavorite;
    private int type;
    private int surface;

    public Features() {
        level = 0;
        directionType = 0;
        terrain = 0;
        isFavorite = false;
        type = 0;
        surface = 0;
    }

    public Features(int level, int directionType, int terrain, boolean isFavorite, int type, int surface) {
        this.level = level;
        this.directionType = directionType;
        this.terrain = terrain;
        this.isFavorite = isFavorite;
        this.type = type;
        this.surface = surface;
    }

    public int getLevel() {
        Log.i("getLevel from Features", "return: " + level);
        return level;
    }

    public void setLevel(int level) {
        Log.i("setLevel from Features", "value: " + level);
        this.level = level;
    }

    public int getDirectionType() {
        Log.i("getDirectionType from Features", "return: " + directionType);
        return directionType;
    }

    public void setDirectionType(int directionType) {
        Log.i("setDirectionType from Features", "value: " + directionType);
        this.directionType = directionType;
    }

    public int getTerrain() {
        Log.i("getTerrain from Features", "return: " + terrain);
        return terrain;
    }

    public void setTerrain(int terrain) {
        Log.i("setTerrain from Features", "value: " + terrain);
        this.terrain = terrain;
    }

    public boolean isFavorite() {
        Log.i("isFavorite from Features", "return: " + isFavorite);
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        Log.i("setFavorite from Features", "value: " + favorite);
        isFavorite = favorite;
    }

    public int getType() {
        Log.i("getType from Features", "return: " + type);
        return type;
    }

    public void setType(int type) {
        Log.i("setType from Features", "value: " + type);
        this.type = type;
    }

    public int getSurface() {
        Log.i("getSurface from Features", "return: " + surface);
        return surface;
    }

    public void setSurface(int surface) {
        Log.i("setSurface from Features", "value: " + surface);
        this.surface = surface;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Level: %d, Direction Type: (%d), Terrain: %d, Favorite?: %b, Type: %s,  Surface: %s",
                level, directionType, terrain, isFavorite, type, surface);
    }
}
