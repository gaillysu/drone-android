package com.dayton.drone.utils;

import com.dayton.drone.model.CanlendarWeek;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by med on 16/5/25.
 */
public class Common {

    public static long ONEDAY = 24*60*60*1000l;


    /**
     * return one day which start 00:00:00
     * @param date : YYYY/MM/DD HH:MM:SS
     * @return : YYYY/MM/DD 00:00:00
     */
    public static Date removeTimeFromDate(Date date)
    {
        Calendar calBeginning = new GregorianCalendar();
        calBeginning.setTime(date);
        calBeginning.set(Calendar.HOUR_OF_DAY, 0);
        calBeginning.set(Calendar.MINUTE, 0);
        calBeginning.set(Calendar.SECOND, 0);
        calBeginning.set(Calendar.MILLISECOND, 0);
        Date today = calBeginning.getTime();
        return today;
    }

    public static CanlendarWeek getThisweek(Date date)
    {
            return new CanlendarWeek(date);
    }

    public static CanlendarWeek getLastweek(Date date)
    {
        CanlendarWeek thisweek = new CanlendarWeek(date);
        Date lastWeekend = new Date(thisweek.getWeekStart().getTime() - ONEDAY);
        return new CanlendarWeek(lastWeekend);
    }

    /**
     *
     * @param date
     * @return the Date  is "date - 6 days"
     */
    public static Date getLast7Days(Date date)
    {
        return new Date(date.getTime()-6*ONEDAY);
    }

    /**
     *
     * @param date
     * @return the Date  is "date - 29 days"
     */
    public static Date getLast30Days(Date date)
    {
        return new Date(date.getTime()-29*ONEDAY);
    }
}
