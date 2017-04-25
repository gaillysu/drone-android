package com.dayton.drone.ble.util;

/**
 * Created by med on 17/4/24.
 * transfer weather WEATHER  to weather code
 * weather WEATHER,see@http://openweathermap.org/weather-conditions
 * weather code,see@WeatherCode
 */

public class WeatherID {

    final int id;
    public WeatherID(int id) {
        this.id = id;
    }
    public  int rawValue() {return id;}

    public enum WEATHER {
        THUNDER_STORM_WITH_LIGHT_RAIN(200),
        THUNDER_STORM_WITH_RAIN(201),
        THUNDER_STORM_WITH_HEAVY_RAIN(202),
        LIGHT_THUNDER_STORM(210),
        THUNDER_STORM(211),
        HEAVY_THUNDER_STORM(212),
        RAGGED_THUNDER_STORM(221),
        THUNDER_STORM_WITH_LIGHT_DRIZZLE(230),
        THUNDER_STORM_WITH_DRIZZLE(231),
        THUNDER_STORM_WITH_HEAVY_DRIZZLE(232),
        LIGHT_INTENSITY_DRIZZLE(300),
        DRIZZLE(301),
        HEAVY_INTENSITY_DRIZZLE(302),
        LIGHT_INTENSITY_DRIZZLE_RAIN(310),
        DRIZZLE_RAIN(311),
        HEAVY_INTENSITY_DRIZZLE_RAIN(312),
        SHOWER_RAIN_AND_DRIZZLE(313),
        HEAVY_SHOWER_RAIN_AND_DRIZZLE(314),
        SHOWER_DRIZZLE(321),
        LIGHT_RAIN(500),
        MODERATE_RAIN(501),
        HEAVY_INTENSITY_RAIN(502),
        VERY_HEAVY_RAIN(503),
        EXTREME_RAIN(504),
        FREEZING_RAIN(511),
        LIGHT_INTENSITY_SHOWER_RAIN(520),
        SHOWER_RAIN(521),
        HEAVY_INTENSITY_SHOWER_RAIN(522),
        RAGGED_SHOWER_RAIN(531),
        LIGHT_SNOW(600),
        SNOW(601),
        HEAVY_SNOW(602),
        SLEET(611),
        SHOWER_SLEET(612),
        LIGHT_RAIN_AND_SNOW(615),
        RAIN_AND_SNOW(616),
        LIGHT_SHOWER_SNOW(620),
        SHOWER_SNOW(621),
        HEAVY_SHOWER_SNOW(622),
        MIST(701),
        SMOKE(711),
        HAZE(721),
        FOG(741),
        DUST(761),
        CLEAR_SKY(800),
        FEW_CLOUDS(801),
        SCATTERED_CLOUDS(802),
        BROKEN_CLOUDS(803),
        OVERCAST_CLOUDS(804),
        TORNADO(900),
        TYPHOON(901),
        HURRICANE(902),
        WINDY(905),
        LIGHT_BREEZE(952),
        GENTLE_BREEZE(953),
        MODERATE_BREEZE(954),
        FRESH_BREEZE(955),
        STRONG_BREEZE(956),
        HIGH_WIND(957),
        GALE(958),
        SEVERE_GALE(959),
        STORM(960);

        final int id;

        WEATHER(int id) {
            this.id = id;
        }
        public  int rawValue() {return id;}
    }

    private boolean equal(WEATHER weather){
        return this.rawValue()== weather.rawValue();
    }

    public WeatherCode convertID2Code(boolean day)
    {
        if(equal(WEATHER.CLEAR_SKY)) {
            return day?WeatherCode.CLEAR_DAY:WeatherCode.CLEAR_NIGHT;
        }
        if(equal(WEATHER.FEW_CLOUDS)) {
            return day?WeatherCode.PARTLY_CLOUDY_DAY:WeatherCode.PARTLY_CLOUDY_NIGHT;
        }
        if(equal(WEATHER.SCATTERED_CLOUDS) || equal(WEATHER.BROKEN_CLOUDS) || equal(WEATHER.OVERCAST_CLOUDS)) {
            return WeatherCode.CLOUDY;
        }
        if(equal(WEATHER.TORNADO)) {
            return WeatherCode.TORNADO;
        }
        if(equal(WEATHER.TYPHOON)) {
            return WeatherCode.TYPHOON;
        }
        if(equal(WEATHER.HURRICANE)) {
            return WeatherCode.HURRICANE;
        }
        if(equal(WEATHER.WINDY) || equal(WEATHER.LIGHT_BREEZE) || equal(WEATHER.GENTLE_BREEZE) || equal(WEATHER.MODERATE_BREEZE) || equal(WEATHER.FRESH_BREEZE) || equal(WEATHER.STRONG_BREEZE) || equal(WEATHER.HIGH_WIND) || equal(WEATHER.GALE) || equal(WEATHER.SEVERE_GALE))
        {
            return WeatherCode.WINDY;
        }

        if(equal(WEATHER.STORM) || equal(WEATHER.THUNDER_STORM_WITH_LIGHT_RAIN) || equal(WEATHER.THUNDER_STORM_WITH_RAIN) || equal(WEATHER.THUNDER_STORM_WITH_HEAVY_RAIN) || equal(WEATHER.LIGHT_THUNDER_STORM)
                || equal(WEATHER.THUNDER_STORM) || equal(WEATHER.HEAVY_THUNDER_STORM) || equal(WEATHER.RAGGED_THUNDER_STORM) || equal(WEATHER.THUNDER_STORM_WITH_LIGHT_DRIZZLE) || equal(WEATHER.THUNDER_STORM_WITH_DRIZZLE) || equal(WEATHER.THUNDER_STORM_WITH_HEAVY_DRIZZLE) )
        {
            return WeatherCode.STORMY;
        }

        if(equal(WEATHER.LIGHT_SNOW) || equal(WEATHER.SNOW) || equal(WEATHER.HEAVY_SNOW) || equal(WEATHER.SLEET) || equal(WEATHER.SHOWER_SLEET) || equals(WEATHER.LIGHT_RAIN_AND_SNOW) || equals(WEATHER.RAIN_AND_SNOW) || equals(WEATHER.LIGHT_SHOWER_SNOW) || equals(WEATHER.SHOWER_SNOW) || equals(WEATHER.HEAVY_SHOWER_SNOW))
        {
            return WeatherCode.SNOW;
        }

        if(equal(WEATHER.MIST) || equal(WEATHER.SMOKE) || equal(WEATHER.HAZE) || equal(WEATHER.FOG) || equal(WEATHER.DUST)) {
            return WeatherCode.FOG;
        }

        if(equal(WEATHER.LIGHT_INTENSITY_DRIZZLE) || equal(WEATHER.DRIZZLE) || equal(WEATHER.HEAVY_INTENSITY_DRIZZLE) || equal(WEATHER.LIGHT_INTENSITY_DRIZZLE_RAIN) || equal(WEATHER.DRIZZLE_RAIN) || equal(WEATHER.LIGHT_RAIN)) {
            return WeatherCode.RAIN_LIGHT;
        }
        if(equal(WEATHER.HEAVY_INTENSITY_DRIZZLE_RAIN) || equal(WEATHER.SHOWER_RAIN_AND_DRIZZLE) || equal(WEATHER.HEAVY_SHOWER_RAIN_AND_DRIZZLE) || equal(WEATHER.SHOWER_DRIZZLE)
                || equal(WEATHER.MODERATE_RAIN) || equal(WEATHER.HEAVY_INTENSITY_RAIN) || equal(WEATHER.VERY_HEAVY_RAIN) || equal(WEATHER.EXTREME_RAIN)
                || equal(WEATHER.FREEZING_RAIN) || equal(WEATHER.LIGHT_INTENSITY_SHOWER_RAIN) || equal(WEATHER.SHOWER_RAIN) || equal(WEATHER.HEAVY_INTENSITY_SHOWER_RAIN) || equal(WEATHER.RAGGED_SHOWER_RAIN)) {
            return WeatherCode.RAIN_HEAVY;
        }
        return WeatherCode.INVALID_DATA;
    }

}
