package com.wpi.cs4518.werideshare.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mrampiah on 12/7/16.
 */


/**
 * This class acts as a data structure to package the depart and return times for a schedule.
 *
 */
public class ScheduleTime {

    private GregorianCalendar departTime, returnTime;

    public ScheduleTime(GregorianCalendar departTime, GregorianCalendar returnTime){
        this.departTime = departTime;
        this.returnTime = returnTime;
    }

    public GregorianCalendar getDepartTime() {
        return departTime;
    }

    public void setDepartTime(GregorianCalendar departTime) {
        this.departTime = departTime;
    }

    public GregorianCalendar getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(GregorianCalendar returnTime) {
        this.returnTime = returnTime;
    }


    @Override
    public boolean equals(Object other){
        if(! (other instanceof ScheduleTime) )
            return false;

        return departTime.equals( ((ScheduleTime) other).getDepartTime()) &&
                returnTime.equals( ( (ScheduleTime) other).getReturnTime());
    }
}
