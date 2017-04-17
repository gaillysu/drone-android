package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/4/14.
 */

public class GetWeatherModel {
    //pls refer to  https://openweathermap.org/current#current_JSON

    class Coordinate {
        float lon;
        float lat;
    }

    class Weather {
        int id;
        String main;
        String description;
        String icon;
    }

    class Main {
         float temp;
         float pressure;
         float humidity;
         float temp_min;
         float temp_max;
    }

    class Wind {
        float speed;
        float deg;
    }

    class Clouds {
        int all;
    }

    class Sys {
        int type;
        int id;
        float message;
        String country;
        long sunrise;
        long sunset;
    }

    private Coordinate coord;
    private Weather[] weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int id;
    private String name;
    private int cod;

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
