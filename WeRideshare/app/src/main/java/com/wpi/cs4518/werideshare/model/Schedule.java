package com.wpi.cs4518.werideshare.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrampiah on 12/7/16.
 */


/**
 * This class holds the schedule information for each user.
 * Each schedule has an associated origin and destination, along with a map of which days and times
 * the ride is made.
 */
public class Schedule implements Serializable {


    //enum to hold values for each day of the week
    public enum DayOfWeek{
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }

    String id, name;
    private WeRideShareLocation origin, destination;
    Map<String, ScheduleTime> times;


    public Schedule(){
        //required for firebase
    }
    /**
     * Constructor, missing 'times' and period parameters for convenience; set to new HashMap (default)
     * @param origin
     * @param destination
     */
    public Schedule(String id, String name, WeRideShareLocation origin,
                    WeRideShareLocation destination){
        this(id, name, origin, destination, new HashMap<String, ScheduleTime>());
    }

    /**
     * Fully loaded constructor
     * @param origin: location the ride starts from
     * @param destination: location the ride ends
     * @param times: days and times for the scheduled ride
     */
    public Schedule(String id, String name, WeRideShareLocation origin,
                    WeRideShareLocation destination, Map<String, ScheduleTime> times){
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.times = times;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private WeRideShareLocation getOrigin() {
        return origin;
    }

    private void setOrigin(WeRideShareLocation origin) {
        this.origin = origin;
    }

    private WeRideShareLocation getDestination() {
        return destination;
    }

    private void setDestination(WeRideShareLocation destination) {
        this.destination = destination;
    }

    public Map<String, ScheduleTime> getTimes() {
        return times;
    }

    public void setTimes(Map<String, ScheduleTime> times) {
        this.times = times;
    }

    /**
     * Method to add a day and associated times to the current schedule.
     * @param day
     * @param time
     */
    public void addScheduleTime(DayOfWeek day, ScheduleTime time){
        times.put(day.toString(), time); //replaces current value of 'day' if not null
    }

    @Override
    public String toString(){
        return String.format("Schdeule: %s", name);
    }

    /*
     * Schedule workflow
     * - enter schedule information
     * - get map data from maps fragment
     * - save writes to firebase
     * - on each schedule, have button "find matches"
     * - brings up matches fragment, with "message" button
     * - add user to chats and being conversationing
     */
}
