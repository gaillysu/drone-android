package com.dayton.drone.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by boy on 2016/4/27.
 */
public class MenuBean {
    private Drawable drawable;
    private String mDec;

    public String getDec() {
        return mDec;
    }

    public void setDec(String dec) {
        mDec = dec;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}