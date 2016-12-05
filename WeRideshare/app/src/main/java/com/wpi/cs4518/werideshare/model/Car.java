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

    public Car(String carName, String registrationId, int capacity, String userId){
        this.capacity = capacity;
        this.registrationId = registrationId;
        this.carName = carName;
        this.userId = userId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
