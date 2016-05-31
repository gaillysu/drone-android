package com.dayton.drone.activity;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.StepsHandler;
import com.dayton.drone.view.CalendarView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/22.
 */
public class ActivitiesActivity extends BaseActivity implements OnChartValueSelectedListener {

    @Bind(R.id.home_fragment_progress_bar)
    MagicProgressCircle mProgressBar;

    @Bind(R.id.activities_progress_middle_tv)
    TextView homeMiddleTv;
    @Bind(R.id.activities_progress_middle_user_step_goal)
    TextView userStepGoalTextView;

    @Bind(R.id.fragment_home_title_calories)
    TextView caloriesTextView;

    @Bind(R.id.fragment_home_title_km)
    TextView kmTextView;

    @Bind(R.id.fragment_home_title_active_time)
    TextView activeTimeTextView;

    @Bind(R.id.home_content_bar)
    RelativeLayout guideBar;

    @Bind(R.id.hourly_header_layout_guide_layout)
    LinearLayout titleDec;

    @Bind(R.id.activity_activities_hourly_bar)
    BarChart hourlyBarChart;

    @Bind(R.id.activity_activities_weekly_line)
    LineChart thisWeekLineChart;

    @Bind(R.id.activity_activities_last_weekly_line)
    LineChart lastWeekLineChart;

    @Bind(R.id.activity_activities_monthly_line)
    LineChart lastMonthLineChart;

    @Bind(R.id.activities_activity_shadow_home_guide_view)
    RelativeLayout guideView;

    @Bind(R.id.activities_guide_title_dec)
    TextView titleGuide;

    @Bind(R.id.activities_guide_dec_actvies)
    TextView activitiesGuide;

    @Bind(R.id.activities_guide_dec_bar)
    TextView barGuide;

    @Bind(R.id.activities_calendar_title_date_tv)
    TextView mTitleCalendarTextView;

    @Bind(R.id.activities_calendar_group)
    LinearLayout calendarGroup;

    @Bind(R.id.calendar_date_view)
    CalendarView calendar;

    @Bind(R.id.activities_activity_calendar_back_month)
    ImageButton backMonth;

    @Bind(R.id.activities_activity_title_next_month)
    ImageButton nextMonth;

    @Bind(R.id.activities_guide_dec_chart)
    TextView chartGuideDec;

    @Bind(R.id.activities_activity_title_date)
    LinearLayout showCalendar;

    private Date selectedDate = new Date(); //the selected date comes from calendar.
    private int guidePage = 1;
    private boolean isShowCalendar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        ButterKnife.bind(this);

        calendarGroup.setVisibility(View.GONE);
        boolean mIsFirst = SpUtils.getBoolean(this, CacheConstants.IS_FIRST, true);
        mProgressBar.setStartColor(R.color.progress_start_color);
        mProgressBar.setEndColor(R.color.progress_end_color);
        mProgressBar.setSmoothPercent(1.0f*SpUtils.getIntMethod(this, CacheConstants.TODAY_STEP, 0)/SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000));
        homeMiddleTv.setText(SpUtils.getIntMethod(this, CacheConstants.TODAY_STEP, 0) + "");
        userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal)
                + SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000));
        calendar.setSelectMore(false);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);

        modifyChart(hourlyBarChart);
        modifyChart(lastMonthLineChart);
        modifyChart(lastWeekLineChart);
        modifyChart(thisWeekLineChart);

        drawGraph();
    }

    private void drawGraph()
    {
        StepsHandler stepsHandler = new StepsHandler(getModel().getStepsDatabaseHelper(),getModel().getUser());
        setDataInChart(hourlyBarChart,stepsHandler.getDailySteps(selectedDate));
        setDataInChart(thisWeekLineChart, stepsHandler.getThisWeekSteps(selectedDate));
        setDataInChart(lastWeekLineChart, stepsHandler.getLastWeekSteps(selectedDate));
        setDataInChart(lastMonthLineChart, stepsHandler.getLastMonthSteps(selectedDate));
    }
    private void setDataInChart(BarChart barChart, DailySteps dailySteps){
        SimpleDateFormat sdf = new SimpleDateFormat("HH: mm");
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();
        for (int i = 0; i < dailySteps.getHourlySteps().length; i++) {
            yValue.add(new BarEntry(dailySteps.getHourlySteps()[i], i));
            xVals.add(i+":00");
        }
        barChart.setScaleMinima((.14f), 1f);
        BarDataSet dataSet = new BarDataSet(yValue, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{getResources().getColor(R.color.colorPrimaryDark)});
        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);
        BarData data = new BarData(xVals, dataSets);
        barChart.setData(data);
        barChart.invalidate();
    }
    private void setDataInChart(LineChart lineChart, List<DailySteps> stepsList){
        SimpleDateFormat sdf = new SimpleDateFormat("d'/'M");
        List<String> xVals = new ArrayList<String>();
        List<Entry> yValue = new ArrayList<Entry>();

        for (int i = 0; i < stepsList.size(); i++) {
            DailySteps dailySteps = stepsList.get(i);
            yValue.add(new Entry(dailySteps.getDailySteps(), i));
            xVals.add(sdf.format(new Date(dailySteps.getDate())));
        }
        LineDataSet set = new LineDataSet(yValue, "");
        set.setColor(R.color.grey);
        set.setCircleColor(R.color.grey);
        set.setLineWidth(1.5f);
        set.setCircleSize(3.0f);
        set.setDrawCircleHole(true);
        set.setFillAlpha(128);
        set.setDrawFilled(true);
        set.setDrawValues(false);
        set.setCircleColorHole(Color.BLACK);
        set.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.chart_gradient);
        set.setFillDrawable(drawable);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);
        LineData data = new LineData(xVals, dataSets);
        lineChart.setData(data);
        lineChart.animateY(2, Easing.EasingOption.EaseInCirc);
        lineChart.invalidate();
    }

    private void modifyChart(BarChart barChart){
        calendar.setCalendarData(new Date());
        mTitleCalendarTextView.setText(new SimpleDateFormat("MMM").format(selectedDate));
        barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.setOnChartValueSelectedListener(this);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDragEnabled(true);
        barChart.setSelected(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setLabelCount(3, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinValue(0.0f);
        leftAxis.setValueFormatter(new YAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                int resValue = (int) value;
                return resValue+"";
            }
        });
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setAxisLineColor(R.color.grey);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLimitLinesBehindData(false);
        rightAxis.setDrawLabels(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
    }
    private void modifyChart(LineChart lineChart){
        lineChart.setContentDescription("");
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("");
        lineChart.setNoDataText("");
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);

        LimitLine limitLine = new LimitLine(7000f, "Goal");
        limitLine.setLineWidth(1.5f);
        limitLine.setLineColor(R.color.grey);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLine.setTextSize(18f);
        limitLine.setTextColor(R.color.grey);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisLineColor(R.color.grey);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.addLimitLine(limitLine);
        leftAxis.setAxisMinValue(0.0f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setAxisLineColor(R.color.grey);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLimitLinesBehindData(false);
        rightAxis.setDrawLabels(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisLineColor(R.color.grey);
        xAxis.setTextColor(R.color.grey);
        xAxis.setDrawLimitLinesBehindData(false);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @OnClick(R.id.activities_activity_shadow_home_guide_view)
    public void guideViewClick(){
        ++guidePage;
        switch (guidePage) {
            case 2:
                titleDec.setBackgroundResource(R.drawable.user_guide_bg);
                showCalendar.setBackgroundDrawable(new BitmapDrawable());
                activitiesGuide.setVisibility(View.VISIBLE);
                titleGuide.setVisibility(View.GONE);
                break;
            case 3:
                titleDec.setBackgroundDrawable(new BitmapDrawable());
                guideBar.setBackgroundResource(R.drawable.user_guide_bg);
                activitiesGuide.setVisibility(View.GONE);
                barGuide.setVisibility(View.VISIBLE);
                break;
            case 4:
                barGuide.setVisibility(View.GONE);
                hourlyBarChart.setBackgroundResource(R.drawable.user_guide_bg);
                guideBar.setBackgroundDrawable(new BitmapDrawable());
                chartGuideDec.setVisibility(View.VISIBLE);
                break;
            case 5:
                chartGuideDec.setVisibility(View.GONE);
                guideView.setVisibility(View.GONE);
                SpUtils.putBoolean(getModel(), CacheConstants.IS_FIRST, false);
                break;
        }
    }

    @OnClick(R.id.activities_activity_calendar_back_month)
    public void mIvBackMouthClick(){
        String leftMouth = calendar.clickLeftMonth();
        mTitleCalendarTextView.setText(leftMouth);
    }

    @OnClick(R.id.activities_activity_title_next_month)
    public void mIvNextMouthClick() {
        String rightMouth = calendar.clickRightMonth();
        mTitleCalendarTextView.setText(rightMouth);
    }

    @OnClick(R.id.activities_activity_title_date)
    public void showCalendarClick() {
        nextMonth.setVisibility(View.VISIBLE);
        backMonth.setVisibility(View.VISIBLE);
        calendarGroup.setVisibility(View.VISIBLE);
        isShowCalendar = true;
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                selectedDate = downDate;
                nextMonth.setVisibility(View.GONE);
                backMonth.setVisibility(View.GONE);
                calendarGroup.setVisibility(View.GONE);
                drawGraph();
            }
        });
    }


    @OnClick(R.id.activities_title_back)
    public void backOnClick(){
        if(isShowCalendar){
            calendarGroup.setVisibility(View.GONE);
            nextMonth.setVisibility(View.GONE);
            backMonth.setVisibility(View.GONE);
            isShowCalendar = false;
        }else{
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isShowCalendar){
                calendarGroup.setVisibility(View.GONE);
                nextMonth.setVisibility(View.GONE);
                backMonth.setVisibility(View.GONE);
                isShowCalendar = false;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
                homeMiddleTv.setText(event.getSteps() + "");
                userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal)
                        + event.getGoal());
                mProgressBar.setSmoothPercent(1.0f * event.getSteps() / event.getGoal());
                SpUtils.putIntMethod(getApplicationContext(),CacheConstants.GOAL_STEP,event.getGoal());
                SpUtils.putIntMethod(getApplicationContext(),CacheConstants.TODAY_STEP,event.getSteps());
            }
        });
    }

    @Subscribe
    public void onEvent(final BigSyncEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(event.getStatus() == BigSyncEvent.BIG_SYNC_EVENT.STOPPED)
                {
                    drawGraph();
                }
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        hourlyBarChart.highlightValue(e.getXIndex(), dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
    }
}