package com.wpi.cs4518.werideshare.model;

/**
 * Created by mrampiah on 11/13/16.
 */

public class Vehicle {
    /*

        String vehicleTableQuery = "CREATE TABLE vehicle ("
                + "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "registration_id VARCHAR2(30) UNIQUE, "
                + "capacity INTEGER NOT NULL"
                + ");";
     */

    private int vehicleId, capacity;
    private String registrationId;
}
