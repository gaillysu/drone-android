package com.dayton.drone.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class TutorialViewpagerAdapter extends PagerAdapter {

    private List<ImageView> imageIdArray;
    public TutorialViewpagerAdapter(Context context , List<ImageView> imageIdArray){
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
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        container.addView(imageView);
        return imageView;
    }
}
