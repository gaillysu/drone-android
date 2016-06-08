package com.dayton.drone.utils;

import com.dayton.drone.model.CanlendarMonth;
import com.dayton.drone.model.CanlendarWeek;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by med on 16/5/25.
 */
public class Common {

    public static long ONE_DAY = 24*60*60*1000L;


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
        return calBeginning.getTime();
    }

    public static CanlendarWeek getThisweek(Date date)
    {
            return new CanlendarWeek(date);
    }

    public static CanlendarWeek getLastweek(Date date)
    {
        CanlendarWeek thisweek = new CanlendarWeek(date);
        Date lastWeekend = new Date(thisweek.getWeekStart().getTime() - ONE_DAY);
        return new CanlendarWeek(lastWeekend);
    }

    public static CanlendarMonth getThisMonth(Date date){
        return new CanlendarMonth(date);
    }

    public static CanlendarMonth getLastMonth(Date date){
        CanlendarMonth canlendarMonth = new CanlendarMonth(date);
        Date lastMonthDate = new Date(canlendarMonth.getMonthStart().getTime() - ONE_DAY);
        return new CanlendarMonth(lastMonthDate);
    }

    public static Date getLast7Days(Date date)
    {
        return new Date(date.getTime()-6* ONE_DAY);
    }

    public static Date getLast30Days(Date date)
    {
        return new Date(date.getTime()-29* ONE_DAY);
    }
}
