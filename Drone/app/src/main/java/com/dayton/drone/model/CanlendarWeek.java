package com.dayton.drone.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by med on 16/5/25.
 */
public  class CanlendarWeek {
    private final Date currentDay;
    private Date weekStart;
    private  Date weekEnd;

    public CanlendarWeek(Date currentDay) {
        this.currentDay = currentDay;
        CalculateWeekDuration();
    }
    private void CalculateWeekDuration()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDay);
        weekStart =  new Date(currentDay.getTime()-(cal.get(Calendar.DAY_OF_WEEK)-1)*24*60*60*1000L);
        weekEnd = new Date(weekStart.getTime()+6*24*60*60*1000L);
    }

    public Date getCurrentDay() {
        return currentDay;
    }

    public Date getWeekStart() {
        return weekStart;
    }

    public Date getWeekEnd() {
        return weekEnd;
    }
}