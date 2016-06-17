package com.dayton.drone.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by med on 16/5/27.
 */
public class DailySteps {
    /**
     *  date, one day only with yyyy-MM-dd, starts at middle night
     */
    final private long date;

    final private int stepsGoal;

    /**
     * hourly steps for 24 hours
     */
    final private int[] hourlySteps;

    private int dailySteps;

    public DailySteps(long date, @NonNull int[] hourlySteps,int stepsGoal) {
        this.date = date;
        this.hourlySteps = hourlySteps;
        this.stepsGoal = stepsGoal;
        for(int i=0;i<hourlySteps.length;i++)
        {
            this.dailySteps += hourlySteps[i];
        }
    }

    public long getDate() {
        return date;
    }

    public int[] getHourlySteps() {
        return hourlySteps;
    }

    public int getDailySteps() {
        return dailySteps;
    }

    public int getDailyStepsGoal() {
        return stepsGoal;
    }
}
