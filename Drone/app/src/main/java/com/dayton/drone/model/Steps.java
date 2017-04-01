package com.dayton.drone.model;

import org.json.JSONArray;
import org.json.JSONException;

public class Steps  implements Comparable<Steps>{

    final private int timeFrameSteps;
    final private long timeFrame;
    private int id = (int) Math.floor(Math.random()*Integer.MAX_VALUE);
    private String userID;
    private long date;
    private String hourlySteps = "[[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0]]";
    private int distance;
    private int cloudID = -1;
    private int stepsGoal = 10000;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getHourlySteps() {
        return hourlySteps;
    }

    public void setHourlySteps(String hourlySteps) {
        this.hourlySteps = hourlySteps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCloudID() {
        return cloudID;
    }

    public void setCloudID(int cloudID) {
        this.cloudID = cloudID;
    }

    public int getStepsGoal() {
        return stepsGoal;
    }

    public void setStepsGoal(int stepsGoal) {
        this.stepsGoal = stepsGoal;
    }


    public Steps()
    {
        this(0,0);
    }

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
