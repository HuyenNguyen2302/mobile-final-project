package com.wpi.cs4518.werideshare.model;

/**
 * Created by mrampiah on 12/7/16.
 */
public class Location {
    String name, description;
    double xcoord, ycoord;

    public Location(String name, String description, double xcoord, double ycoord){
        this.name = name;
        this.description = description;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

}
