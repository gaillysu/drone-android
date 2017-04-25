package com.dayton.drone.model;

import com.dayton.drone.ble.model.WeatherUpdateModel;

/**
 * Created by med on 17/4/18.
 */

public class Weather {
    String city;
    short temperature;
    int weatherCode; //see@WeatherUpdateModel.WeatherCode
}
