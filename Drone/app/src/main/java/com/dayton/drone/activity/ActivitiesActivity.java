package com.dayton.drone.activity;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.DownloadStepsEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.event.StepsGoalChangedEvent;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.model.Steps;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.Common;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.StepsHandler;
import com.dayton.drone.view.CalendarView;
import com.dayton.drone.view.TipsView;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.ble.util.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Math.abs;

public class ActivitiesActivity extends BaseActivity implements OnChartValueSelectedListener {

    @Bind(R.id.home_fragment_progress_bar)
    MagicProgressCircle mProgressBar;

    @Bind(R.id.activities_progress_middle_tv)
    TextView homeMiddleTv;
    @Bind(R.id.activities_progress_middle_user_step_goal)
    TextView userStepGoalTextView;

    @Bind(R.id.activity_activities_hourly_bar)
    BarChart hourlyBarChart;

    @Bind(R.id.activity_activities_weekly_line)
    LineChart thisWeekLineChart;
    @Bind(R.id.activity_activities_last_weekly_line)
    LineChart lastWeekLineChart;
    @Bind(R.id.activity_activities_monthly_line)
    LineChart lastMonthLineChart;
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


    //day
    @Bind(R.id.fragment_home_title_calories)
    TextView caloriesTextView;
    @Bind(R.id.fragment_home_title_km)
    TextView kmTextView;
    @Bind(R.id.fragment_home_title_active_time)
    TextView activeTimeTextView;
    //this week
    @Bind(R.id.weekly_header_layout_calories_textview_this_week)
    TextView textViewCalories;
    @Bind(R.id.weekly_header_layout_active_time_textview_this_week)
    TextView textViewActivityTime;
    @Bind(R.id.weekly_header_layout_km_textview_this_week)
    TextView textViewDistance;
    //last week
    @Bind(R.id.weekly_header_layout_calories_textview_last)
    TextView textViewCaloriesLast;
    @Bind(R.id.weekly_header_layout_active_time_textview_last)
    TextView textViewActivityTimeLast;
    @Bind(R.id.weekly_header_layout_km_textview_last)
    TextView textViewDistanceLast;
    //last month
    @Bind(R.id.weekly_header_layout_calories_textview_last_month)
    TextView textViewCaloriesLastMonth;
    @Bind(R.id.weekly_header_layout_active_time_textview_last_month)
    TextView textViewActivityTimeLastMonth;
    @Bind(R.id.weekly_header_layout_km_textview_last_month)
    TextView textViewDistanceLastMonth;

    private Date selectedDate = new Date(); //the selected date comes from calendar.
    private int guidePage = 1;
    private boolean isShowCalendar = false;
    private StepsDatabaseHelper stepsDatabaseHelper;

    private GoogleApiClient client;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        calendarGroup.setVisibility(View.GONE);
        calendar.setSelectMore(false);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);
        stepsDatabaseHelper = getModel().getStepsDatabaseHelper();
        date = new Date(System.currentTimeMillis());
        modifyChart(hourlyBarChart);
        modifyChart(thisWeekLineChart);
        modifyChart(lastWeekLineChart);
        modifyChart(lastMonthLineChart);
        drawCalculateData(true);
        drawGraph(true);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private enum ActivityTimeSlot {
        DAFAULT, THISWEEK, LASTWEEK, LASTMONTH;
    }

    private void drawCalculateData(boolean all) {
        setCurrentDayData(selectedDate);
        setThisWeekData(selectedDate);
        if(all) {
            setLastWeekData(selectedDate);
            setLastMonthData(selectedDate);
        }
    }
    private void drawGraph(boolean all) {
        StepsHandler stepsHandler = new StepsHandler(getModel().getStepsDatabaseHelper(), getModel().getUser());
        setDataInProgressBar(stepsHandler.getDailySteps(selectedDate));
        setDataInChart(hourlyBarChart, stepsHandler.getDailySteps(selectedDate));
        if (all) {
            setDataInChart(thisWeekLineChart, stepsHandler.getThisWeekSteps(selectedDate));
            setDataInChart(lastWeekLineChart, stepsHandler.getLastWeekSteps(selectedDate));
            setDataInChart(lastMonthLineChart, stepsHandler.getLast30DaysSteps(selectedDate));
        }
    }

    private void setDataInProgressBar(DailySteps dailySteps) {
        int steps = SpUtils.getIntMethod(this, CacheConstants.TODAY_STEP, 0);
        if (SpUtils.getBoolean(this,CacheConstants.TODAY_RESET,false)){
            steps += SpUtils.getIntMethod(this,CacheConstants.TODAY_BASESTEP,0);
        }
        int goal = SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000);
        //when user select a history date, show its data with that day
        if (Common.removeTimeFromDate(selectedDate).getTime() != Common.removeTimeFromDate(new Date()).getTime()
                || !getModel().getSyncController().isConnected()) {
            steps = dailySteps.getDailySteps();
            goal = dailySteps.getDailyStepsGoal();
        }
        SpUtils.printAllConstants(this);
        mProgressBar.setSmoothPercent(1.0f * steps / goal);
        homeMiddleTv.setText(steps + "");
        userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal) + goal);
    }

    private void setDataInChart(BarChart barChart, DailySteps dailySteps) {
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();
        int maxValue = 0;
        final int stepsModulo = 200;
        for (int i = 0; i < dailySteps.getHourlySteps().length; i++) {
            yValue.add(new BarEntry(dailySteps.getHourlySteps()[i], i));
            xVals.add(i + ":00");
            if (dailySteps.getHourlySteps()[i] > maxValue) {
                maxValue = dailySteps.getHourlySteps()[i];
            }
        }
        //For better user experience, I set the Y value is multiple of 10
        int labelCount = 6;
        if (maxValue == 0) {
            maxValue = 500;
        } else{
            maxValue = maxValue + abs(stepsModulo - (maxValue % stepsModulo));
            labelCount = (maxValue/stepsModulo) +1;
        }
        barChart.getAxisLeft().setAxisMaxValue(maxValue);
        barChart.getAxisLeft().setLabelCount(labelCount,true);

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

    private void setDataInChart(LineChart lineChart, List<DailySteps> stepsList) {
        SimpleDateFormat sdf = new SimpleDateFormat("d'/'M", Locale.US);
        List<String> xVals = new ArrayList<String>();
        List<Entry> yValue = new ArrayList<Entry>();
        int maxValue = 0;
        final int goal = SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000);
        final int stepsModulo = 500;
        for (int i = 0; i < stepsList.size(); i++) {
            DailySteps dailySteps = stepsList.get(i);
            int steps = dailySteps.getDailySteps();
            if (dailySteps.getDailySteps() > maxValue){
                maxValue = steps;
            }
            yValue.add(new Entry(steps, i));
            xVals.add(sdf.format(new Date(dailySteps.getDate())));
        }
        Log.w("Karl","Max vlaue = " + maxValue);
        boolean putTop = false;
        if (maxValue == 0 ||  maxValue  < goal){
            maxValue = goal + stepsModulo;
        }else{
            putTop = true;
            maxValue = maxValue + abs(stepsModulo - (maxValue % stepsModulo));
        }

        LimitLine limitLine = new LimitLine(goal, "Goal: " +  goal);
        limitLine.setLineWidth(1.5f);
        limitLine.setLineColor(R.color.grey);
        limitLine.setTextSize(18f);
        limitLine.setTextColor(R.color.grey);

        if(putTop) {
            limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        }else{
            limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
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

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.addLimitLine(limitLine);
        leftAxis.setAxisMaxValue(maxValue * 1.0f);
        LineData data = new LineData(xVals, dataSets);
        lineChart.setData(data);

        lineChart.animateY(2, Easing.EasingOption.EaseInCirc);
        lineChart.invalidate();
    }

    private void modifyChart(BarChart barChart) {
        calendar.setCalendarData(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String day = dateFormat.format(new Date());
        mTitleCalendarTextView.setText(day.split("-")[2] + " " + new SimpleDateFormat("MMM", Locale.US).format(selectedDate));
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

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinValue(0.0f);
        leftAxis.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                int resValue = (int) value;
                return resValue + "";
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

    private void modifyChart(LineChart lineChart) {
        lineChart.setContentDescription("");
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("");
        lineChart.setNoDataText("");
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        TipsView tipsView = new TipsView(this, R.layout.custom_marker_view);
        lineChart.setMarkerView(tipsView);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisLineColor(R.color.grey);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
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

    private void setThisWeekData(Date date) {
        List<Steps> thisWeekSteps = stepsDatabaseHelper.getThisWeekSteps(getModel().getUser().getUserID(), date);
        setTitleData(thisWeekSteps, ActivityTimeSlot.THISWEEK);
    }

    private void setLastWeekData(Date date) {
        List<Steps> lastWeekList = stepsDatabaseHelper.getLastWeekSteps(getModel().getUser().getUserID(), date);
        setTitleData(lastWeekList, ActivityTimeSlot.LASTWEEK);
    }

    private void setLastMonthData(Date date) {
        List<Steps> lastMonthList = stepsDatabaseHelper.getLastMonthSteps(getModel().getUser().getUserID(), date);
        setTitleData(lastMonthList, ActivityTimeSlot.LASTMONTH);
    }

    private void setTitleData(List<Steps> list, ActivityTimeSlot thisWeek) {
        int stepsAccount = 0;
        int activityTime = 0;
        for (int x = 0; x < list.size(); x++) {
            Steps steps = list.get(x);
            stepsAccount += steps.getDailySteps();
            activityTime += steps.getDailyActiveTime();
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        Double stepsLength = (getModel().getUser().getHeight() * 0.45) / 100;
        Double distance = stepsLength * stepsAccount / 1000;

        String time = formatTimeActivity(activityTime);
        String calories = df.format((2.0 * getModel().getUser().getWeight() * 3.5) / 200 * activityTime) + "";
        String distanceTotal = df.format(distance) + "";
        switch (thisWeek) {
            case THISWEEK:
                textViewActivityTime.setText(time);
                textViewDistance.setText(distanceTotal);
                textViewCalories.setText(calories);
                break;

            case LASTWEEK:
                textViewActivityTimeLast.setText(time);
                textViewDistanceLast.setText(distanceTotal);
                textViewCaloriesLast.setText(calories);
                break;

            case LASTMONTH:
                textViewActivityTimeLastMonth.setText(time);
                textViewDistanceLastMonth.setText(distanceTotal);
                textViewCaloriesLastMonth.setText(distanceTotal);
                break;

        }
    }

    private void setCurrentDayData(Date date) {
        Steps steps;
        DecimalFormat df = new DecimalFormat("######0.00");
        List<Optional<Steps>> list = stepsDatabaseHelper.get(getModel().getUser().getUserID(), date);
        if(list.isEmpty())
        {
            steps = new Steps(0,date.getTime());
        } else {
            steps = list.get(0).get();
        }

        caloriesTextView.setText(df.format(calculationCalories(steps)));
        kmTextView.setText(df.format(calculateDistance(steps)));
        activeTimeTextView.setText(formatTimeActivity(steps.getDailyActiveTime()));
    }

    private double calculationCalories(Steps steps) {
        int activityTime = steps.getDailyActiveTime();
        return 2.0 * getModel().getUser().getWeight() * 3.5 / 200 * activityTime;
    }

    private double calculateDistance(Steps steps) {
        double stepsLength = getModel().getUser().getHeight() * 0.45 / 100;
        return stepsLength * steps.getDailySteps() / 1000;
    }

    private String formatTimeActivity(int timeActive) {

        StringBuffer buffer = new StringBuffer();
        if (timeActive == 0) {
            return buffer.append("0").toString();
        }
        if (timeActive % 60 > 1) {
            int hour = timeActive / 60;
            buffer.append(hour + "h");
            String mis = timeActive - hour * 60 + "";
            if (new Integer(mis).intValue() < 10) {
                mis = "0" + mis;
            }
            buffer.append(mis + "m");
        } else {
            buffer.append(timeActive + "m");
        }
        return buffer.toString();
    }

    @OnClick(R.id.activities_activity_calendar_back_month)
    public void mIvBackMonthClick() {
        Date leftMouth = calendar.clickLeftMonth();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-mm-dd");
        String leftdate = simple.format(leftMouth);
        mTitleCalendarTextView.setText(leftdate.split("-")[2] + " " + new SimpleDateFormat("MMM", Locale.US).format(leftMouth));
    }

    @OnClick(R.id.activities_activity_title_next_month)
    public void mIvNextMonthClick() {
        Date rightMouth = calendar.clickRightMonth();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-mm-dd");
        String rightdate = simple.format(rightMouth);
        mTitleCalendarTextView.setText(rightdate.split("-")[2] + " " + new SimpleDateFormat("MMM", Locale.US).format(rightMouth));
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
                date = downDate;
                selectedDate = downDate;
                nextMonth.setVisibility(View.GONE);
                backMonth.setVisibility(View.GONE);
                calendarGroup.setVisibility(View.GONE);
                mTitleCalendarTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(downDate).split("-")[2] + " " +
                        new SimpleDateFormat("MMM", Locale.US).format(downDate));
                drawCalculateData(true);
                drawGraph(true);
                List<Optional<Steps>> stepsList = getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(), selectedDate);
                if (stepsList.isEmpty()) {
                    Log.w("Karl","Downloading steps");
                    getModel().getSyncActivityManager().downloadSteps(selectedDate);
                }
            }
        });
    }

    @OnClick(R.id.activities_title_back)
    public void backOnClick() {
        if (isShowCalendar) {
            calendarGroup.setVisibility(View.GONE);
            nextMonth.setVisibility(View.GONE);
            backMonth.setVisibility(View.GONE);
            isShowCalendar = false;
        } else {
            finish();
        }
    }

    @OnClick(R.id.activities_title_set_goal_button)
    public void setGoal() {
        final String[] goals = getResources().getStringArray(R.array.steps_goal_array);
        List<String> stringList = new ArrayList<>(Arrays.asList(goals));
        stringList.add(getString(R.string.activity_activities_customize_goal));
        CharSequence[] cs = stringList.toArray(new CharSequence[stringList.size()]);
        new MaterialDialog.Builder(this)
                .title(R.string.set_goal_popup_title)
                .items(cs)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which >= 0) {
                            if(which<=2) {
                                SpUtils.putIntMethod(ActivitiesActivity.this, CacheConstants.GOAL_STEP, Integer.valueOf(goals[which]));
                                EventBus.getDefault().post(new StepsGoalChangedEvent(Integer.valueOf(goals[which])));
                            }
                            else if(which == 3)
                            {
                                new MaterialDialog.Builder(ActivitiesActivity.this)
                                        .title(R.string.activity_activities_set_goal)
                                        .content(R.string.activity_activities_customize_goal_hint)
                                        .inputType(InputType.TYPE_CLASS_NUMBER)
                                        .input(getString(R.string.activity_activities_customize_goal_hint), "",
                                                new MaterialDialog.InputCallback() {
                                                    @Override
                                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                                        if (input.length() == 0)
                                                            return;
                                                        int steps = Integer.parseInt(input.toString());
                                                        SpUtils.putIntMethod(ActivitiesActivity.this, CacheConstants.GOAL_STEP, steps);
                                                        EventBus.getDefault().post(new StepsGoalChangedEvent(steps));
                                                    }
                                                }).negativeText(R.string.set_goal_popup_button_cancel)
                                        .show();
                            }
                        }
                        return true;
                    }
                })
                .negativeText(R.string.set_goal_popup_button_cancel)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowCalendar) {
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
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * @param event every 10s, do little sync
     */
    @Subscribe
    public void onEvent(final LittleSyncEvent event) {
        Log.w("Karl","Little sync triggered?");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                drawCalculateData(false);
                drawGraph(false);
            }
        });
    }

    /**
     * @param event every 5minutes, do big sync
     */
    @Subscribe
    public void onEvent(final BigSyncEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getStatus() == BigSyncEvent.BIG_SYNC_EVENT.STOPPED) {
                    drawCalculateData(true);
                    drawGraph(true);
                }
            }
        });
    }

    /**
     * when user select one day, if no any steps record in the local, download it from cloud server and refresh graph
     *
     * @param event
     */
    @Subscribe
    public void onEvent(final DownloadStepsEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getStatus() == DownloadStepsEvent.DOWNLOAD_STEPS_EVENT.STOPPED) {
                    drawCalculateData(true);
                    drawGraph(true);
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

    private Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Activities Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}