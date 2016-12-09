package com.wpi.cs4518.werideshare.model;

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
public class Schedule {


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

    WeRideShareLocation origin, destination;
    Map<DayOfWeek, ScheduleTime> times;

    //the range of dates for which this schedule is valid
    GregorianCalendar startOfPeriod, endOfPeriod;


    /**
     * Constructor, missing 'times' parameter for convenience; set to new HashMap (default)
     * @param origin
     * @param destination
     * @param startOfPeriod
     * @param endOfPeriod
     */
    public Schedule(WeRideShareLocation origin, WeRideShareLocation destination,
                    GregorianCalendar startOfPeriod, GregorianCalendar endOfPeriod){
       this(origin, destination, new HashMap<DayOfWeek, ScheduleTime>(), startOfPeriod, endOfPeriod );
    }

    /**
     * Fully loaded constructor
     * @param origin: location the ride starts from
     * @param destination: location the ride ends
     * @param times: days and times for the scheduled ride
     * @param startOfPeriod: start date for which schedule is valid
     * @param endOfPeriod: end date for which schedule is valid
     */
    public Schedule(WeRideShareLocation origin, WeRideShareLocation destination,
                    Map<DayOfWeek, ScheduleTime> times,
                    GregorianCalendar startOfPeriod, GregorianCalendar endOfPeriod){
        this.origin = origin;
        this.destination = destination;
        this.times = times;
        this.startOfPeriod = startOfPeriod;
        this.endOfPeriod = endOfPeriod;
    }

    public WeRideShareLocation getOrigin() {
        return origin;
    }

    public void setOrigin(WeRideShareLocation origin) {
        this.origin = origin;
    }

    public WeRideShareLocation getDestination() {
        return destination;
    }

    public void setDestination(WeRideShareLocation destination) {
        this.destination = destination;
    }

    public Map<DayOfWeek, ScheduleTime> getTimes() {
        return times;
    }

    public void setTimes(Map<DayOfWeek, ScheduleTime> times) {
        this.times = times;
    }

    public GregorianCalendar getStartOfPeriod() {
        return startOfPeriod;
    }

    public void setStartOfPeriod(GregorianCalendar startOfPeriod) {
        this.startOfPeriod = startOfPeriod;
    }

    public GregorianCalendar getEndOfPeriod() {
        return endOfPeriod;
    }

    public void setEndOfPeriod(GregorianCalendar endOfPeriod) {
        this.endOfPeriod = endOfPeriod;
    }

    /**
     * Method to add a day and associated times to the current schedule.
     * @param day
     * @param time
     */
    public void addScheduleTime(DayOfWeek day, ScheduleTime time){
        times.put(day, time); //replaces current value of 'day' if not null
    }
}
