package com.dayton.drone.database.bean;

import org.json.JSONArray;
import org.json.JSONException;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by med on 17/3/30.
 */

public class StepsBean extends RealmObject {

    @Ignore
    final private int timeFrameSteps;
    @Ignore
    final private long timeFrame;

    /**
     * field name and initialize value, Primary field
     */
    @Ignore
    public static final String fID = "id";
    private int id = (int) Math.floor(Math.random()*Integer.MAX_VALUE);

    /**
     * which user ID
     */
    @Ignore
    public static final String fUserID = "userID";
    private String userID;


    /**
     *  date, one day which is Year/Month/Day
     */
    @Ignore
    public static final String fDate = "date";
    private long date;


    /**
     * this is accumulation steps for every hour a day, like this [[1,2,...12],,....,[24,2,3,...12]]
     */
    @Ignore
    public static final String fHourlySteps = "hourlySteps";
    private String hourlySteps = "[[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0],[0]]";

    /**
     * one day's total distance ,unit is meter.
     */
    @Ignore
    public static final String fDistance = "distance";
    private int distance;


    /**
     * the cloudID is the unique value that saved on Cloud server, "-1" menas that no sync with cloud server
     */
    @Ignore
    public static final String fCloudID = "cloudID";
    private int cloudID = -1;

    /**
     * every day perhaps has different goal,default is 10000 steps per day
     */
    @Ignore
    public static final String fStepsGoal = "stepsGoal";
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


    public StepsBean()
    {
        this(0,0);
    }

    public StepsBean(int timeFrameSteps, long timeFrame) {
        this.timeFrameSteps = timeFrameSteps;
        this.timeFrame = timeFrame;
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
