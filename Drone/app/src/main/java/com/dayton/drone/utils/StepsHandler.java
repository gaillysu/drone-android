package com.dayton.drone.utils;

import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.model.CanlendarMonth;
import com.dayton.drone.model.CanlendarWeek;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.model.Steps;
import com.dayton.drone.model.User;

import net.medcorp.library.ble.util.Optional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by med on 16/5/27.
 */
public class StepsHandler {

    final private StepsDatabaseHelper stepsDatabaseHelper;
    final private User user;

    public StepsHandler(StepsDatabaseHelper stepsDatabaseHelper, User user) {
        this.stepsDatabaseHelper = stepsDatabaseHelper;
        this.user = user;
    }

    public DailySteps getDailySteps(Date date)
    {
        Date middleNight = Common.removeTimeFromDate(date);
        List<Optional<Steps>> stepsList = stepsDatabaseHelper.get(user.getUserID(),date);
        int[] hours = new int[24];
        for (Optional<Steps> steps: stepsList){
            int hourIndex = new Date(steps.get().getTimeFrame()).getHours();
            hours[hourIndex] = hours[hourIndex] + steps.get().getSteps();
        }
        return new DailySteps(middleNight.getTime(),hours);
    }

    public List<DailySteps> getThisWeekSteps(Date date)
    {
        List<DailySteps> dailyStepsList = new ArrayList<>();
        CanlendarWeek canlendarWeek =  Common.getThisweek(date);
        for(long start = canlendarWeek.getWeekStart().getTime();start<=canlendarWeek.getWeekEnd().getTime();start+=Common.ONEDAY)
        {
            dailyStepsList.add(getDailySteps(new Date(start)));
        }
        return dailyStepsList;
    }

    public List<DailySteps> getLastWeekSteps(Date date)
    {
        List<DailySteps> dailyStepsList = new ArrayList<>();
        CanlendarWeek canlendarWeek =  Common.getLastweek(date);
        for(long start = canlendarWeek.getWeekStart().getTime();start<=canlendarWeek.getWeekEnd().getTime();start+=Common.ONEDAY)
        {
            dailyStepsList.add(getDailySteps(new Date(start)));
        }
        return dailyStepsList;
    }

    public List<DailySteps> getLastMonthSteps(Date date)
    {
        List<DailySteps> dailyStepsList = new ArrayList<>();
        CanlendarMonth canlendarMonth = Common.getLastMonth(date);
        for(long start = canlendarMonth.getMonthStart().getTime();start<=canlendarMonth.getMonthEnd().getTime();start+=Common.ONEDAY)
        {
            dailyStepsList.add(getDailySteps(new Date(start)));
        }
        return dailyStepsList;
    }
}
