package com.example.messivsronaldo.Model;

public class PlayerRecord {
    private String name;
    private int score;
    private double longitude = 0.0;
    private double latitude = 0.0;

    public PlayerRecord() {

    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public PlayerRecord setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public PlayerRecord setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public PlayerRecord setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public PlayerRecord setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public String toString() {
        return "PlayerRecord{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}


