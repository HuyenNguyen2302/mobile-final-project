package com.wpi.cs4518.werideshare;

import android.location.Location;

import com.wpi.cs4518.werideshare.model.Schedule;

import java.sql.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Yuan Wen on 12/8/2016.
 */


public class ScheduleMatcher {
    /**
     *  * Takes in two schedules, riderSchedule and driverSchedule
     * and a mile radius, allowed to be a float
     * and a minutes allowed to deviate
     *
     * to return true if these two schedules fall within that mile and minute radius.
     * otherwise false
     * @param riderSchedule a rider's schedule
     * @param driverSchedule a driver's schedule
     * @param mileRadius a float amount of miles a rider can allow
     * @param minutesDeviation a int amount of minutes a rider will allow deviate
     * @return
     */
    static boolean isGoodMatch(Schedule riderSchedule, Schedule driverSchedule, float mileRadius, int minutesDeviation){
        //check miles
        //check origin
        Location s1Start = new Location("service Provider");
        s1Start.setLatitude(riderSchedule.getOrigin().getLatitude()); //we have our own definition of WeRideShareLocation as well as android Defintion
        s1Start.setLongitude(riderSchedule.getOrigin().getLongitude());
        Location s2Start = new Location("service Provider");
        s2Start.setLatitude(driverSchedule.getOrigin().getLatitude()); //we have our own definition of WeRideShareLocation as well as android Defintion
        s2Start.setLongitude(driverSchedule.getOrigin().getLongitude());
        //distance To : Returns the approximate distance in meters between this location and the given location.
        double radiusInMeters = mileRadius * 1609.34;
        if(s1Start.distanceTo(s2Start) > radiusInMeters){
            return false;
        }

        //check destination
        Location s1End = new Location("riderSchedule end");
        s1End.setLatitude(riderSchedule.getDestination().getLatitude()); //we have our own definition of WeRideShareLocation as well as android Defintion
        s1End.setLongitude(riderSchedule.getDestination().getLongitude());
        Location s2End = new Location("driverSchedule end");
        s2End.setLatitude(driverSchedule.getDestination().getLatitude());
        s2End.setLongitude(driverSchedule.getDestination().getLongitude());
        if(s1End.distanceTo(s2End) > radiusInMeters){
            return false;
        }

        //check the minutes deviation of schedule per day
        for (Schedule.DayOfWeek day : Schedule.DayOfWeek.values()){
            try {
                GregorianCalendar departTimeS1 = riderSchedule.getTimes().get(day).getDepartTime();
                GregorianCalendar departTimeS2 = driverSchedule.getTimes().get(day).getDepartTime();

                //if the difference betwen the rider and driver scheduels is more than minutes deviation, this is not a fit
                long differenceInMillisDepart = Math.abs(departTimeS1.getTimeInMillis() - departTimeS2.getTimeInMillis());
                if (differenceInMillisDepart > (minutesDeviation * 60000)) {
                    return false;
                }
                //do the same for return time
                GregorianCalendar returnTimeS1 = riderSchedule.getTimes().get(day).getDepartTime();
                GregorianCalendar returnTimeS2 = driverSchedule.getTimes().get(day).getDepartTime();

                //if the difference betwen the rider and driver scheduels is more than minutes deviation, this is not a fit
                long differenceInMillisReturn = Math.abs(returnTimeS1.getTimeInMillis() - returnTimeS2.getTimeInMillis());
                if (differenceInMillisReturn > (minutesDeviation * 60000)) {
                    return false;
                }
            }
            catch (Exception e){
                // most likely a time was not defined for that day
                continue;
            }
        }


        //if rider is in date range of the driver, then this is a match!
        if(driverSchedule.getStartOfPeriod().getTime().before(riderSchedule.getStartOfPeriod().getTime())
                &&
                driverSchedule.getEndOfPeriod().getTime().after(riderSchedule.getEndOfPeriod().getTime()))
            return true;
        //if the times match up exactly
        if(driverSchedule.getStartOfPeriod().getTime().equals(riderSchedule.getStartOfPeriod().getTime())
                &&
                driverSchedule.getEndOfPeriod().getTime().equals(riderSchedule.getEndOfPeriod().getTime()))
            return true;
        return false;

    }
}
