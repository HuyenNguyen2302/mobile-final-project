package com.wpi.cs4518.werideshare.model;

/**
 * Created by mrampiah on 11/13/16.
 */

public class Car {
    /*

        String vehicleTableQuery = "CREATE TABLE vehicle ("
                + "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "registration_id VARCHAR2(30) UNIQUE, "
                + "capacity INTEGER NOT NULL"
                + ");";
     */

    private int capacity;
    private String registrationId, carName, userId;

    public Car(){
        //empty constructor for fragment creation
    }

    public Car(String carName, String registrationId, int capacity, String userId){
        this.capacity = capacity;
        this.registrationId = registrationId;
        this.carName = carName;
        this.userId = userId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getCarName() {
        return carName;
    }

    public String getUserId() {
        return userId;
    }

}
