package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dayton.drone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/13.
 */
public class GalleryListAdapter extends BaseAdapter {
    private Context context;
    private int[] imagesIdArray;

    public GalleryListAdapter(Context context, int[] imagesArray) {
        this.context = context;
        this.imagesIdArray = imagesArray;
    }

    @Override
    public int getCount() {
        return imagesIdArray.length != 0 ? imagesIdArray.length : 0;
    }

    @Override
    public Object getItem(int i) {
        return imagesIdArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.adapter_gallery_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
            holder.deviceImageView.setBackgroundResource(imagesIdArray[i]);
        return view;
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.gallery_adapter_item_iv)
        ImageView deviceImageView;
    }
}
