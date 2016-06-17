package com.dayton.drone.model;

import com.dayton.drone.database.bean.StepsBean;

import org.json.JSONArray;
import org.json.JSONException;

public class Steps extends StepsBean implements Comparable<Steps>{

    final private int timeFrameSteps;
    final private long timeFrame;

    public Steps(int timeFrameSteps, long timeFrame) {
        this.timeFrameSteps = timeFrameSteps;
        this.timeFrame = timeFrame;
    }

    @Override
    public int compareTo(Steps another) {
        if (getDate() < another.getDate()){
            return -1;
        }else if(getDate() > another.getDate()){
            return 1;
        }
        return 0;
    }

    /**
     *
     * @return the accumulation steps a day.
     */
    public int getDailySteps()
    {
        int dailySteps = 0;
        try {
            JSONArray hourlySteps = new JSONArray(getHourlySteps());
            //24 hours
            for(int hour =0;hour<hourlySteps.length();hour++)
            {
                JSONArray minutesInHour = hourlySteps.optJSONArray(hour);
                //12 * 5 minutes
                for(int fiveMinutes=0;fiveMinutes<minutesInHour.length();fiveMinutes++)
                {
                    dailySteps += minutesInHour.optInt(fiveMinutes);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailySteps;
    }

    /**
     *
     * @return daily active time,unit in "minute"
     */

    public int getDailyActiveTime()
    {
        int active_time = 0;
        try {
            JSONArray hourlySteps = new JSONArray(getHourlySteps());
            //24 hours
            for(int hour =0;hour<hourlySteps.length();hour++)
            {
                JSONArray minutesInHour = hourlySteps.optJSONArray(hour);
                //12 * 5 minutes
                for(int fiveMinutes=0;fiveMinutes<minutesInHour.length();fiveMinutes++)
                {
                    if(minutesInHour.optInt(fiveMinutes)>0)
                    {
                        active_time = active_time + 5;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return active_time;
    }

    public int getTimeFrameSteps() {
        return timeFrameSteps;
    }

    public long getTimeFrame() {
        return timeFrame;
    }
}
