package com.wpi.cs4518.werideshare.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mrampiah on 12/7/16.
 */


/**
 * This class acts as a data structure to package the depart and return times for a schedule.
 *
 */
public class ScheduleTime implements Serializable {
    //ONLY USE THE HOUR, MINUTE, SECOND of GREGORIAN CALENDAR
    //the year, month, day are null
    private Time departTime, returnTime;

    public ScheduleTime(){
        //required for firebase
    }

    public ScheduleTime(Time departTime, Time returnTime){
        this.departTime = departTime;
        this.returnTime = returnTime;
    }

    public ScheduleTime(String[] departTimeValues, String[] returnTimeValues){
        this.departTime = parseValues(departTimeValues);
        this.returnTime = parseValues(returnTimeValues);
    }

    private Time getDepartTime() {
        return departTime;
    }

    public long getDepartTimeMillis(){
        return departTime.getTime();
    }

    private void setDepartTime(Time departTime) {
        this.departTime = departTime;
    }

    public void setDepartTimeMillis(long millis){
        this.departTime = new Time(millis);
    }

    private Time getReturnTime() {
        return returnTime;
    }

    public long getReturnTimeMillis(){
        return returnTime.getTime();
    }

    private void setReturnTime(Time returnTime) {
        this.returnTime = returnTime;
    }

    public void setReturnTimeMillis(long millis){
        this.returnTime = new Time(millis);
    }


    @Override
    public boolean equals(Object other){
        if(! (other instanceof ScheduleTime) )
            return false;

        return departTime.equals( ((ScheduleTime) other).getDepartTime()) &&
                returnTime.equals( ( (ScheduleTime) other).getReturnTime());
    }

    public static Time parseValues(String[] values){
        if (values.length < 2)
            throw new IllegalArgumentException();

        int hours = Integer.parseInt(values[0]);
        int minutes = Integer.parseInt(values[1]);

        return new Time(hours, minutes, 0);

    }
}
