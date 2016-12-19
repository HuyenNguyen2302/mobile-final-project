package com.wpi.cs4518.werideshare.model;

import java.io.Serializable;
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

    private String id;
    private String name;
    private Map<String, ScheduleTime> times;


    public Schedule(){
        //required for firebase
    }
    /**
     * Constructor, missing 'times' and period parameters for convenience; set to new HashMap (default)
     *
     * Problem synopsis      Actual value of parameter ''destination'' is always ''null'' (at line 47)
     *
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
        WeRideShareLocation origin1 = origin;
        WeRideShareLocation destination1 = destination;
        this.times = times;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, ScheduleTime> getTimes() {
        return times;
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
