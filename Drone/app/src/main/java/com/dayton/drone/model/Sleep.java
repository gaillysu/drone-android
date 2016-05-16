package com.dayton.drone.model;

import com.dayton.drone.database.bean.SleepBean;

/**
 * Created by gaillysu on 15/11/17.
 */
public class Sleep extends SleepBean implements Comparable<Sleep>{

    @Override
    public int compareTo(Sleep another) {
        if (getDate() < another.getDate()){
            return -1;
        }else if(getDate() > another.getDate()){
            return 1;
        }
        return 0;
    }


}
