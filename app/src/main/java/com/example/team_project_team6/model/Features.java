package com.example.team_project_team6.model;

import android.util.Log;

import java.util.Locale;

public class Features {
    private int level; // easy, medium, or difficult
    private int directionType; // loop or out-and-back
    private int terrain; // hilly or flat
    private boolean isFavorite;
    private int type; // street or trail
    private int surface; // even or uneven

    public Features() {
        Log.i("empty Features constructor", "Initializing all values");
        level = 0;
        directionType = 0;
        terrain = 0;
        isFavorite = false;
        type = 0;
        surface = 0;
    }

    public Features(int level, int directionType, int terrain, boolean isFavorite, int type, int surface) {
        Log.i("Features constructor with all parameters", "Initializing all values");
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

    public void findLevel(String level) {
        Log.i("setLevel from Features", "value: " + level);
        switch (level) {
            case "Easy":
                this.level = 1;
                break;
            case "Medium":
                this.level = 2;
                break;
            case "Hard":
                this.level = 3;
                break;
            default:
                this.level = 0;
                break;
        }
    }

    public int getDirectionType() {
        Log.i("getDirectionType from Features", "return: " + directionType);
        return directionType;
    }

    public void setDirectionType(int directionType) {
        Log.i("setDirectionType from Features", "value: " + directionType);
        this.directionType = directionType;
    }

    public void findDirectionType(String directionType) {
        Log.i("setDirectionType from Features", "value: " + directionType);
        switch (directionType) {
            case "Loop":
                this.directionType = 1;
                break;
            case "Out and Back":
                this.directionType = 2;
                break;
            default:
                this.directionType = 0;
                break;
        }
    }

    public int getTerrain() {
        Log.i("getTerrain from Features", "return: " + terrain);
        return terrain;
    }

    public void setTerrain(int terrain) {
        Log.i("setTerrain from Features", "value: " + terrain);
        this.terrain = terrain;
    }

    public void findTerrain(String terrain) {
        Log.i("setTerrain from Features", "value: " + terrain);
        switch (terrain) {
            case "Flat":
                this.terrain = 1;
                break;
            case "Hilly":
                this.terrain = 2;
                break;
            default:
                this.terrain = 0;
                break;
        }
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

    public void findType(String type) {
        Log.i("setType from Features", "value: " + type);
        switch (type) {
            case "Street":
                this.type = 1;
                break;
            case "Trail":
                this.type = 2;
                break;
            default:
                this.type = 0;
                break;
        }
    }

    public int getSurface() {
        Log.i("getSurface from Features", "return: " + surface);
        return surface;
    }

    public void setSurface(int surface) {
        Log.i("setSurface from Features", "value: " + surface);
        this.surface = surface;
    }

    public void findSurface(String surface) {
        Log.i("setSurface from Features", "value: " + surface);
        switch (surface) {
            case "Even":
                this.surface = 1;
                break;
            case "Uneven":
                this.surface = 2;
                break;
            default:
                this.surface = 0;
                break;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Level: %d, Direction Type: %d, Terrain: %d, Favorite?: %b, Type: %s,  Surface: %s",
                level, directionType, terrain, isFavorite, type, surface);
    }
}
