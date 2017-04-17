package com.dayton.drone.ble.util;

import com.dayton.drone.ble.model.WeatherUpdateModel.WeatherCode;

/**
 * Created by med on 17/4/17.
 *
 * transfer weather ID  to weather code
 * weather ID,see@http://openweathermap.org/weather-conditions
 * weather code,see@WeatherUpdateModel.WeatherCode
 */

public class WeatherID2Code {
    /**
     *
     * @param id : comes from GetWeatherModel.Weather.id
     * @return WeatherCode
     */
    static public WeatherCode getWeatherCodeByID(int id,boolean day) {
        if(id == 800) {
            return day?WeatherCode.ClearDay:WeatherCode.ClearNight;
        }
        if(id == 801) {
            return day?WeatherCode.PartlyCloudyDay:WeatherCode.PartlyCloudyNight;
        }
        if(id == 802 || id == 803 ||id == 804 ) {
            return WeatherCode.Cloudy;
        }
        if(id == 900) {
            return WeatherCode.Tornado;
        }
        if(id == 901) {
            return WeatherCode.Typhoon;
        }
        if(id == 902) {
            return WeatherCode.Hurricane;
        }
        if(id == 905 || (id >= 952 && id<=959)) {
            return WeatherCode.Windy;
        }
        if(id == 960 || id == 200 || id == 201 || id == 202 ||id == 210 ||id == 211 ||id == 212 ||id == 221 ||id == 230 ||id == 231 ||id == 232 ) {
            return WeatherCode.Stormy;
        }

        if(id == 600 || id == 601 || id == 602 || id == 611 || id == 612 || id == 615 || id == 616 || id == 620 || id == 621 || id == 622)
        {
            return WeatherCode.Snow;
        }

        if(id == 701 || id == 711 || id == 721 || id == 741 || id == 761) {
            return WeatherCode.Fog;
        }

        if(id == 300 || id == 301 || id == 302 || id == 310 || id == 311 || id ==500) {
            return WeatherCode.RainLight;
        }
        if(id == 312 || id == 313 || id == 314 || id == 321 || id == 501 || id == 502 || id == 503 || id == 504 || id == 511 || id == 520 || id == 521 || id == 522 || id == 531) {
            return WeatherCode.RainHeavy;
        }
        return WeatherCode.InvalidData;
    }
}
