package com.dayton.drone.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class WelcomeViewpagerAdapter extends PagerAdapter {

    private List<ImageView> imageIdArray;

    public WelcomeViewpagerAdapter(List<ImageView> imageIdArray){
        this.imageIdArray = imageIdArray;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position%imageIdArray.size();
        ImageView imageView = imageIdArray.get(position);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        container.addView(imageView);
        return imageView;
    }
}
