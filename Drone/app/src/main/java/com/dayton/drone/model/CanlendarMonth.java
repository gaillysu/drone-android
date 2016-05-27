package com.dayton.drone.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by med on 16/5/27.
 */
public class CanlendarMonth {
    final Date currentDay;
    private Date monthStart;
    private  Date monthEnd;

    public CanlendarMonth(Date currentDay) {
        this.currentDay = currentDay;
        CalculateMonthDuration();
    }

    private void CalculateMonthDuration()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDay);
        cal.set(Calendar.DAY_OF_MONTH,1);
        monthStart = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthEnd = cal.getTime();
    }

    public Date getCurrentDay() {
        return currentDay;
    }

    public Date getMonthStart() {
        return monthStart;
    }

    public Date getMonthEnd() {
        return monthEnd;
    }
}
