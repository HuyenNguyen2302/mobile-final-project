package com.wpi.cs4518.werideshare;
import android.content.Context;
import android.location.Location;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import com.wpi.cs4518.werideshare.model.Schedule;
import com.wpi.cs4518.werideshare.model.ScheduleTime;
import com.wpi.cs4518.werideshare.model.WeRideShareLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.Console;
import java.sql.Time;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Yuan Wen on 12/8/2016.
 */
@RunWith(AndroidJUnit4.class)
public class ScheduleMatcherTest {

    ScheduleMatcherTest(){
        testSameEverything();
        testLongDisatnce();
    }

    //this test should return true because the all are the schedules are the same
    @Test
    public void testSameEverything(){

        //this trip is 48.9 miles away wpi -> boston
        WeRideShareLocation wpi =  new WeRideShareLocation("wpi", "best school",42.2741724, -71.8104003 );
        WeRideShareLocation boston =  new WeRideShareLocation("boston", "a city", 42.3133521, -71.1271969);
        Map<Schedule.DayOfWeek, ScheduleTime> timesRider = new EnumMap<Schedule.DayOfWeek, ScheduleTime>(Schedule.DayOfWeek.class);
        timesRider.put(Schedule.DayOfWeek.Sunday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));//depart at 7 am, return at 5 pm
        timesRider.put(Schedule.DayOfWeek.Monday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Tuesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Wednesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Thursday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Friday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Saturday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));

        GregorianCalendar startDateRider = new GregorianCalendar(2016,1,1); //Mon Feb 01 00:00:00 EST 2016
        GregorianCalendar endDateRider = new GregorianCalendar(2016,1,8);
        //System.out.println(" asdf" + startDateRider.getTime());
        //this is a rider's schedule
        Schedule s1 = new Schedule(wpi, boston, timesRider, startDateRider, endDateRider);


        //this is a drivers schedule
        //this trip is 48.9 miles away wpi -> boston
        WeRideShareLocation wpiDriver =  new WeRideShareLocation("wpi", "best school",42.2741724, -71.8104003 );
        WeRideShareLocation bostonDriver =  new WeRideShareLocation("boston", "a city", 42.3133521, -71.1271969);
        Map<Schedule.DayOfWeek, ScheduleTime> timesDriver = new EnumMap<Schedule.DayOfWeek, ScheduleTime>(Schedule.DayOfWeek.class);
        timesDriver.put(Schedule.DayOfWeek.Sunday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));//depart at 7 am, return at 5 pm
        timesDriver.put(Schedule.DayOfWeek.Monday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Tuesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Wednesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Thursday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Friday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Saturday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));

        GregorianCalendar startDateDriver = new GregorianCalendar(2016,1,1);
        GregorianCalendar endDateDriver = new GregorianCalendar(2016,1,8);

        //this is a rider's schedule
        Schedule s2 = new Schedule(wpi, boston, timesDriver, startDateDriver, endDateDriver);


        assertTrue(ScheduleMatcher.isGoodMatch(s1,s2 ,1, 1));
    }

    //this test should return true because the all are the schedules are the same
    @Test
    public void testLongDisatnce(){


        WeRideShareLocation wpi =  new WeRideShareLocation("far away", "far away location",2.2741724, -71.8104003 );
        //
        WeRideShareLocation boston =  new WeRideShareLocation("boston", "a city", 42.3133521, -71.1271969);
        Map<Schedule.DayOfWeek, ScheduleTime> timesRider = new EnumMap<Schedule.DayOfWeek, ScheduleTime>(Schedule.DayOfWeek.class);
        timesRider.put(Schedule.DayOfWeek.Sunday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));//depart at 7 am, return at 5 pm
        timesRider.put(Schedule.DayOfWeek.Monday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Tuesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Wednesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Thursday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Friday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesRider.put(Schedule.DayOfWeek.Saturday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));

        GregorianCalendar startDateRider = new GregorianCalendar(2016,1,1); //Mon Feb 01 00:00:00 EST 2016
        GregorianCalendar endDateRider = new GregorianCalendar(2016,1,8);
        //System.out.println(" asdf" + startDateRider.getTime());
        //this is a rider's schedule
        Schedule s1 = new Schedule(wpi, boston, timesRider, startDateRider, endDateRider);


        //this is a drivers schedule
        //this trip is 48.9 miles away wpi -> boston
        WeRideShareLocation wpiDriver =  new WeRideShareLocation("wpi", "best school",42.2741724, -71.8104003 );
        WeRideShareLocation bostonDriver =  new WeRideShareLocation("boston", "a city", 42.3133521, -71.1271969);
        Map<Schedule.DayOfWeek, ScheduleTime> timesDriver = new EnumMap<Schedule.DayOfWeek, ScheduleTime>(Schedule.DayOfWeek.class);
        timesDriver.put(Schedule.DayOfWeek.Sunday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));//depart at 7 am, return at 5 pm
        timesDriver.put(Schedule.DayOfWeek.Monday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Tuesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Wednesday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Thursday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Friday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));
        timesDriver.put(Schedule.DayOfWeek.Saturday, new ScheduleTime(new GregorianCalendar(0,0,0,7,0,0), new GregorianCalendar(0,0,0,17,0,0)));

        GregorianCalendar startDateDriver = new GregorianCalendar(2016,1,1);
        GregorianCalendar endDateDriver = new GregorianCalendar(2016,1,8);

        //this is a rider's schedule
        Schedule s2 = new Schedule(wpi, boston, timesDriver, startDateDriver, endDateDriver);


        assertFalse(ScheduleMatcher.isGoodMatch(s1,s2 ,1, 1));
    }

}
