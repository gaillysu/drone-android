package com.dayton.drone.activity;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.model.Steps;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;
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

import net.medcorp.library.ble.util.Optional;

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

    @Bind(R.id.activities_activity_calendar_back_day)
    ImageButton backMonth;

    @Bind(R.id.activities_activity_title_next_day)
    ImageButton nextMonth;

    @Bind(R.id.activities_guide_dec_chart)
    TextView chartGuideDec;

    @Bind(R.id.activities_activity_title_date)
    LinearLayout showCalendar;

    private PopupWindow popupWindow;

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
        mProgressBar.setSmoothPercent(0.3f);
        homeMiddleTv.setText(200 + "");
        userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal)
                + SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000));
        calendar.setSelectMore(false);
        calendar.setOnItemClickListener(onItemClicklistener);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);

        initHourlyData();
        modifyChart(lastMonthLineChart);
        modifyChart(lastWeekLineChart);
        modifyChart(thisWeekLineChart);

        List<Steps> thisWeeksSteps = getModel().getStepsDatabaseHelper().getThisWeekSteps(getModel().getUser().getUserID(), selectedDate);
        // TODO add data for this week and test if this works

        List<Steps> lastWeekSteps = getModel().getStepsDatabaseHelper().getLastWeekSteps(getModel().getUser().getUserID(), selectedDate);
        // TODO add data for last week and test if this works

        List<Steps> lastMonthSteps = getModel().getStepsDatabaseHelper().getLastMonthSteps(getModel().getUser().getUserID(), selectedDate);
        // TODO add data for this month and test if this works

        setDataInChart(lastWeekLineChart, thisWeeksSteps, 7);
        setDataInChart(thisWeekLineChart, lastWeekSteps, 7);
        setDataInChart(lastMonthLineChart, lastMonthSteps, 30);
    }
    public void initHourlyData() {
        calendar.setCalendarData(new Date());
        mTitleCalendarTextView.setText(new SimpleDateFormat("MMM").format(selectedDate));
        hourlyBarChart.setDescription("");
        hourlyBarChart.getLegend().setEnabled(false);
        hourlyBarChart.setOnChartValueSelectedListener(this);
        hourlyBarChart.setPinchZoom(false);
        hourlyBarChart.setDrawGridBackground(false);
        hourlyBarChart.setScaleEnabled(false);
        hourlyBarChart.setDrawValueAboveBar(false);
        hourlyBarChart.setDoubleTapToZoomEnabled(false);
        hourlyBarChart.setDragEnabled(true);
        hourlyBarChart.setSelected(false);


        YAxis leftAxis = hourlyBarChart.getAxisLeft();
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
        YAxis rightAxis = hourlyBarChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setAxisLineColor(R.color.grey);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLimitLinesBehindData(false);
        rightAxis.setDrawLabels(false);

        XAxis xAxis = hourlyBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);

        SimpleDateFormat sdf = new SimpleDateFormat("HH: mm");
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();

        List<Optional<Steps>> stepsList = getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(),selectedDate);
        float[] hours = new float[24];
        for (Optional<Steps> steps: stepsList){
            // TODO Fill hours array  with the steps per hour based on the steps.
            /*
                1. Check steps timestamp, put it into right spot of hours[] Hours represent
                2. Do for every step.
                3. Rest is already done.
             */
        }
            for (int i = 0; i < hours.length; i++) {
                    yValue.add(new BarEntry(hours[i]+ 100, i));
                    xVals.add(i+":00");
            }

        if (stepsList.size() < 24) {
            hourlyBarChart.setScaleMinima((.14f), 1f);
        }else{
            hourlyBarChart.setScaleMinima((stepsList.size()/24f),1f);
        }

        BarDataSet dataSet = new BarDataSet(yValue, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{getResources().getColor(R.color.colorPrimaryDark)});
        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);
        BarData data = new BarData(xVals, dataSets);
        hourlyBarChart.setData(data);
    }

    private void setDataInChart(LineChart lineChart, List<Steps> stepsList, int maxSize){
        SimpleDateFormat sdf = new SimpleDateFormat("d'/'M");
        List<String> xVals = new ArrayList<String>();
        List<Entry> yValue = new ArrayList<Entry>();
        long lastDate = 0;
        for (int i = 0; i < maxSize; i++) {
            if (i < stepsList.size()) {
                Steps steps = stepsList.get(i);
                lastDate = steps.getDate();
                yValue.add(new Entry(steps.getSteps(), i));
                xVals.add(sdf.format(new Date(steps.getTimeFrame())));
            }else{
                lastDate+=86400000;
                yValue.add(new Entry(0, i));
                xVals.add(sdf.format(new Date(lastDate)));
            }
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

    @OnClick(R.id.activities_activity_calendar_back_day)
    public void mIvBackMouthClick() {
        String leftMouth = calendar.clickLeftMonth();
        mTitleCalendarTextView.setText(leftMouth);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);
    }

    @OnClick(R.id.activities_activity_title_next_day)
    public void mIvNextMouthClick() {
        String rightMouth = calendar.clickRightMonth();
        mTitleCalendarTextView.setText(rightMouth);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);
    }

    @OnClick(R.id.activities_calendar_title_date_tv)
    public void showCalendarClick() {
        nextMonth.setVisibility(View.VISIBLE);
        backMonth.setVisibility(View.VISIBLE);
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        popupWindow = new PopupWindow(calendar, display.getWidth(), display.getHeight());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private CalendarView.OnItemClickListener onItemClicklistener = new CalendarView.OnItemClickListener() {
        @Override
        public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
            popupWindow.update();
        }
    };

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
                mProgressBar.setSmoothPercent(1.0f * event.getSteps() / event.getGoal());
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