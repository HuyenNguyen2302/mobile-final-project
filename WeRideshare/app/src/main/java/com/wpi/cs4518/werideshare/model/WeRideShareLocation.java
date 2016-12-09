package com.wpi.cs4518.werideshare.model;

/**
 * Created by mrampiah on 12/7/16.
 */
public class WeRideShareLocation {
    private String name, description;
    private double longitude, latitude;

    public WeRideShareLocation(String name, String description, double longitude, double latitude){
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
