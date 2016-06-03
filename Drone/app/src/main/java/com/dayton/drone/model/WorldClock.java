package com.dayton.drone.model;

import com.dayton.drone.database.bean.WorldClockBean;
import com.dayton.drone.view.SlideView;

import java.io.Serializable;

/**
 * Created by med on 16/5/9.
 */
public class WorldClock extends WorldClockBean implements Serializable{
    public SlideView slideView;
}
