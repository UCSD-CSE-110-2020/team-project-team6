package com.example.team_project_team6.model;

public class Features {
    int level;
    int directionType;
    int terrain;
    boolean isFavorite;
    int type;
    int surface;

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
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDirectionType() {
        return directionType;
    }

    public void setDirectionType(int directionType) {
        this.directionType = directionType;
    }

    public int getTerrain() {
        return terrain;
    }

    public void setTerrain(int terrain) {
        this.terrain = terrain;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }
}
