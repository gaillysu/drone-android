package com.dayton.drone.modle;

/**
 * Created by boy on 2016/4/13.
 */
public class Alarm {
    private int id = 1;
    private byte hour;
    private byte mintue;
    private boolean enable;
    private String lable;

    private Alarm(int id, byte hour, byte mintue, String lable, boolean enable) {
        this.id = id;
        this.enable = enable;
        this.lable = lable;
        this.hour = hour;
        this.mintue = mintue;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", hour=" + hour +
                ", mintue=" + mintue +
                ", enlable=" + enable +
                ", lable='" + lable + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMintue() {
        return mintue;
    }

    public void setMintue(byte mintue) {
        this.mintue = mintue;
    }

    public boolean isEnlable() {
        return enable;
    }

    public void setEnlable(boolean enlable) {
        this.enable = enlable;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }
}
