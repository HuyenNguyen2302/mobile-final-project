package com.wpi.cs4518.werideshare.model;

import java.io.Serializable;
import java.sql.Time;

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
    private final Time departTime;
    private final Time returnTime;

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

    private Time getReturnTime() {
        return returnTime;
    }

    public long getReturnTimeMillis(){
        return returnTime.getTime();
    }


    @Override
    public boolean equals(Object other) {
        return other instanceof ScheduleTime && departTime.equals(((ScheduleTime) other).getDepartTime()) && returnTime.equals(((ScheduleTime) other).getReturnTime());

    }

    private static Time parseValues(String[] values){
        if (values.length < 2)
            throw new IllegalArgumentException();

        int hours = Integer.parseInt(values[0]);
        int minutes = Integer.parseInt(values[1]);

        return new Time(hours, minutes, 0);

    }
}
