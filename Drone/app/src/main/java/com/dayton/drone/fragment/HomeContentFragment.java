package com.dayton.drone.fragment;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.utils.Constance;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.UIUtils;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentFragment extends BaseFragment {
    private boolean mIsFirst = true;
    private Context mContext;
    private MagicProgressCircle demoMpc;
    private TextView demoTv;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);
        ButterKnife.bind(this,view);
        mContext = UIUtils.getContext();
        mIsFirst = SpUtils.getBoolean(mContext, Constance.IS_FIRST,true);
        demoMpc = (MagicProgressCircle) view.findViewById(R.id.demo_mpc);
        demoTv = (TextView) view.findViewById(R.id.demo_tv);
        demoMpc.setStartColor(R.color.progress_start_color);
        demoMpc.setEndColor(R.color.progress_end_color);
        demoMpc.setSmoothPercent(0.5f);
        demoTv.setText(200+"");
        startView();
        return view;
    }

    private void startView() {
        if(mIsFirst){
            //TODO
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe
    public void onEvent(final LittleSyncEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                demoTv.setText(event.getSteps()+"");
                demoMpc.setSmoothPercent(1.0f * event.getSteps() / event.getGoal());
            }
        });
    }
}
