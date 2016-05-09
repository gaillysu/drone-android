package com.dayton.drone.modle;

import com.dayton.drone.database.bean.StepsBean;

public class Steps extends StepsBean implements Comparable<Steps>{

    @Override
    public int compareTo(Steps another) {
        if (getDate() < another.getDate()){
            return -1;
        }else if(getDate() > another.getDate()){
            return 1;
        }
        return 0;
    }
}
