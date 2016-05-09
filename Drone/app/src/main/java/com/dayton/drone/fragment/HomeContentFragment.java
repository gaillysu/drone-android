package com.dayton.drone.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.modle.Steps;
import com.dayton.drone.utils.Constance;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.UIUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import butterknife.Bind;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentFragment extends BaseFragment implements OnChartValueSelectedListener {
    private boolean mIsFirst = true;
    private Context mContext;
    @Bind(R.id.home_fragment_progress_bar)
    MagicProgressCircle mProgressBar;
    @Bind(R.id.home_fragment_progress_middle_tv)
    TextView homeMiddleTv;
    @Bind(R.id.fragment_home_title_calories)
    TextView calories;
    @Bind(R.id.fragment_home_title_miles)
    TextView miles;
    @Bind(R.id.fragment_home_title_active_time)
    TextView activeTime;

    @Bind(R.id.fragment_home_guide_title_active_time)
    LinearLayout guideActiveTime;
    @Bind(R.id.fragment_home_guide_title_miles)
    LinearLayout guideMiles;
    @Bind(R.id.fragment_home_guide_calories)
    LinearLayout guideCalories;
    @Bind(R.id.fragment_home_guide_view)
    RelativeLayout guideView;
    private int guidePage = 1;

    @Bind(R.id.fragment_home_content_bar)
    BarChart  barChart;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);
        ButterKnife.bind(this, view);
        mContext = UIUtils.getContext();
        mIsFirst = SpUtils.getBoolean(mContext, Constance.IS_FIRST, true);

        mProgressBar.setStartColor(R.color.progress_start_color);
        mProgressBar.setEndColor(R.color.progress_end_color);
        mProgressBar.setSmoothPercent(0.3f);
        homeMiddleTv.setText(200 + "");
        startView();
        return view;
    }

    private void startView() {
        if (mIsFirst) {
            guideView.setVisibility(View.VISIBLE);
            guideActiveTime.setBackgroundResource(R.drawable.registe_edit_bg);
        }
    }

    @Override
    protected void initData() {
        barChart.setDescription("");
        barChart.setNoDataTextDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.setOnChartValueSelectedListener(this);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setViewPortOffsets(0.0f, 0.0f, 0.0f, 80.0f);
        barChart.setDragEnabled(true);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);
        yAxis = barChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);

        SimpleDateFormat sdf = new SimpleDateFormat("d'/'M");
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();

        List<Steps>stepsList = new ArrayList<Steps>();
        //sample data
        for(int i=0;i<7;i++)
        {
            Steps sampleSteps = new Steps();
            sampleSteps.setDate(sampleSteps.getTimeFrame()-i*24*60*60*1000);
            sampleSteps.setSteps(new Random().nextInt(10000));
            stepsList.add(sampleSteps);
        }
        int i = 0;
        for(Steps steps:stepsList)
        {
            yValue.add(new BarEntry(new float[]{steps.getSteps()}, i));
            xVals.add(sdf.format(new Date(steps.getDate())));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(yValue, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{getResources().getColor(R.color.line_color)});
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(getResources().getColor(R.color.colorPrimaryDark));
        List<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(dataSet);
        BarData data = new BarData(xVals, dataSets);
        barChart.setData(data);
        barChart.highlightValue(stepsList.size()-1, 0);
    }

    @OnClick(R.id.fragment_home_guide_view)
    public void guideUser(){
        guidePage = guidePage++;
        switch (guidePage){
            case 1:
                guideCalories.setBackgroundResource(R.drawable.registe_edit_bg);
                guideActiveTime.setBackgroundDrawable(new BitmapDrawable());
                break;
            case 2:
                guideMiles.setBackgroundResource(R.drawable.registe_edit_bg);
                guideCalories.setBackgroundDrawable(new BitmapDrawable());
                break;
            case 3:
                mProgressBar.setBackgroundResource(R.drawable.registe_edit_bg);
                guideMiles.setBackgroundDrawable(new BitmapDrawable());
                break;
            case 4:
                mProgressBar.setBackgroundDrawable(new BitmapDrawable());
                SpUtils.putBoolean(mContext,Constance.IS_FIRST,true);
                guideView.setVisibility(View.GONE);
                break;

        }
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
                homeMiddleTv.setText(event.getSteps()+"");
                mProgressBar.setSmoothPercent(1.0f * event.getSteps() / event.getGoal());
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        barChart.highlightValue(e.getXIndex(), dataSetIndex);
    }

    @Override
    public void onNothingSelected() {

    }
}

