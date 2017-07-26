package com.dayton.drone.ble.util;

/**
 * Created by med on 17/7/24.
 */

public class WeatherIcon {
    private final String icon;
    public WeatherIcon(String icon) {
        this.icon = icon;
    }

    public WeatherCode convertIcon2Code()
    {
        //pls refer to  https://darksky.net/dev/docs/response#data-point
        if(icon.equals("clear-day")) {
            return WeatherCode.CLEAR_DAY;
        }
        else if(icon.equals("clear-night")) {
            return WeatherCode.CLEAR_NIGHT;
        }
        else if(icon.equals("rain")) {
            return WeatherCode.RAIN_LIGHT;
        }
        else if(icon.equals("snow")) {
            return WeatherCode.SNOW;
        }
        else if(icon.equals("sleet")) {
            return WeatherCode.SNOW;
        }
        else if(icon.equals("wind")) {
            return WeatherCode.WINDY;
        }
        else if(icon.equals("fog")) {
            return WeatherCode.FOG;
        }
        else if(icon.equals("cloudy")) {
            return WeatherCode.CLOUDY;
        }
        else if(icon.equals("partly-cloudy-day")) {
            return WeatherCode.PARTLY_CLOUDY_DAY;
        }
        else if(icon.equals("partly-cloudy-night")) {
            return WeatherCode.PARTLY_CLOUDY_NIGHT;
        }
        else if(icon.equals("hail")) {
            return WeatherCode.STORMY;
        }
        else if(icon.equals("tornado")) {
            return WeatherCode.TORNADO;
        }
        else if(icon.equals("thunderstorm")) {
            return WeatherCode.STORMY;
        }
        return WeatherCode.INVALID_DATA;
    }
}
