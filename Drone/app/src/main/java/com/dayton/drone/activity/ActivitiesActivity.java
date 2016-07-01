package com.dayton.drone.activity;


import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.DownloadStepsEvent;
import com.dayton.drone.event.LittleSyncEvent;
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

import net.medcorp.library.ble.event.BLEBluetoothOffEvent;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLESearchEvent;
import net.medcorp.library.ble.util.Optional;
import net.medcorp.library.permission.PermissionRequestDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Bind(R.id.weekly_header_layout_calories_textview)
    TextView textViewCalories;
    @Bind(R.id.weekly_header_layout_active_time_textview)
    TextView textViewActivityTime;
    @Bind(R.id.weekly_header_layout_km_textview)
    TextView textViewDistance;


    private Date selectedDate = new Date(); //the selected date comes from calendar.
    private int guidePage = 1;
    private boolean isShowCalendar = false;
    private StepsDatabaseHelper stepsDatabaseHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ActivityTimeSlot dataType;
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
        boolean mIsFirst = SpUtils.getBoolean(this, CacheConstants.IS_FIRST, true);
        calendar.setSelectMore(false);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);
        stepsDatabaseHelper = getModel().getStepsDatabaseHelper();
        date = new Date(System.currentTimeMillis());
        findCalories(date);

        modifyChart(hourlyBarChart, dataType = ActivityTimeSlot.DAFAULT);
        modifyChart(thisWeekLineChart, dataType = ActivityTimeSlot.THISWEEK);
        modifyChart(lastWeekLineChart, dataType = ActivityTimeSlot.LASTWEEK);
        modifyChart(lastMonthLineChart, dataType = ActivityTimeSlot.LASTMONTH);
        drawGraph(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private enum ActivityTimeSlot {
        DAFAULT, THISWEEK, LASTWEEK, LASTMONTH;
    }





    private void drawGraph(boolean all) {
        StepsHandler stepsHandler = new StepsHandler(getModel().getStepsDatabaseHelper(), getModel().getUser());
        setDataInProgressBar(stepsHandler.getDailySteps(selectedDate));
        setDataInChart(hourlyBarChart, stepsHandler.getDailySteps(selectedDate));
        setDataInChart(thisWeekLineChart, stepsHandler.getThisWeekSteps(selectedDate));
        if (all) {
            setDataInChart(lastWeekLineChart, stepsHandler.getLastWeekSteps(selectedDate));
            setDataInChart(lastMonthLineChart, stepsHandler.getLast30DaysSteps(selectedDate));
        }
    }

    private void setDataInProgressBar(DailySteps dailySteps) {
        int steps = SpUtils.getIntMethod(this, CacheConstants.TODAY_STEP, 0);
        int goal = SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000);
        //when user select a history date, show its data with that day
        if (Common.removeTimeFromDate(selectedDate).getTime() != Common.removeTimeFromDate(new Date()).getTime()
                || !getModel().getSyncController().isConnected()) {
            steps = dailySteps.getDailySteps();
            goal = dailySteps.getDailyStepsGoal();
        }
        mProgressBar.setSmoothPercent(1.0f * steps / goal);
        homeMiddleTv.setText(steps + "");
        userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal) + goal);
    }

    private void setDataInChart(BarChart barChart, DailySteps dailySteps) {
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();
        int maxHourlySteps = 0;
        for (int i = 0; i < dailySteps.getHourlySteps().length; i++) {
            yValue.add(new BarEntry(dailySteps.getHourlySteps()[i], i));
            xVals.add(i + ":00");
            if(dailySteps.getHourlySteps()[i]>maxHourlySteps)
            {
                maxHourlySteps = dailySteps.getHourlySteps()[i];
            }
        }
        //For better user experience, I set the Y value is multiple of 10
        if(maxHourlySteps==0){
            barChart.getAxisLeft().setAxisMaxValue(100);
        }else {
            barChart.getAxisLeft().setAxisMaxValue(maxHourlySteps % 10 == 0 ? maxHourlySteps : ((maxHourlySteps + 9) / 10) * 10);
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

    private void setDataInChart(LineChart lineChart, List<DailySteps> stepsList) {
        SimpleDateFormat sdf = new SimpleDateFormat("d'/'M", Locale.US);
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


    private void modifyChart(BarChart barChart, ActivityTimeSlot timeSlot) {
        setChartTileText(timeSlot);
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
        leftAxis.setLabelCount(3, true);
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

    private void modifyChart(LineChart lineChart, ActivityTimeSlot timeSlot) {
        setChartTileText(timeSlot);

        lineChart.setContentDescription("");
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("");
        lineChart.setNoDataText("");
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        TipsView tipsView = new TipsView(this,R.layout.custom_marker_view);
        lineChart.setMarkerView(tipsView);

        LimitLine limitLine = new LimitLine(SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000), "Goal");
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
        leftAxis.setAxisMaxValue(SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000)*1.2f);

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

    //set data
    private void setChartTileText(ActivityTimeSlot timeSlot) {
        //TODO
        switch (timeSlot) {
            case DAFAULT:
                break;
            case THISWEEK:
                getLastMonthData(ActivityTimeSlot.THISWEEK);
                break;
            case LASTWEEK:
                getLastMonthData(ActivityTimeSlot.LASTWEEK);
                break;
            case LASTMONTH:
                getLastMonthData(ActivityTimeSlot.LASTMONTH);
                break;
        }
    }

    private void getLastMonthData(ActivityTimeSlot thisweek) {
        StepsDatabaseHelper databaseHelper = getModel().getStepsDatabaseHelper();

        switch (thisweek) {
            case THISWEEK:
                List<Steps> list = databaseHelper.getThisWeekSteps(getModel().getUser().getUserID(), date);

                setTitileData(list);

                break;
            case LASTWEEK:
                List<Steps> lastWeekList = databaseHelper.getLastWeekSteps(getModel().getUser().getUserID(), date);
                setTitileData(lastWeekList);
                break;
            case LASTMONTH:
                List<Steps> lastMonthList = databaseHelper.getLastMonthSteps(getModel().getUser().getUserID(), date);
                setTitileData(lastMonthList);
                break;
        }

    }

    public void setTitileData(List<Steps> list) {
        int stepsAccount = 0;
        int activityTime = 0;
        for (int x = 0; x < list.size(); x++) {
            Steps steps = list.get(x);
            stepsAccount += steps.getDailySteps();
            activityTime += steps.getDailyActiveTime();
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        Double stepsLenth = (getModel().getUser().getHeight() * 0.45) / 100;
        Double distance = stepsLenth * stepsAccount / 100;
        textViewActivityTime.setText(formatTimeActivity(activityTime));
        textViewDistance.setText(df.format(distance) + "");
        textViewCalories.setText(df.format((2.0 * getModel().getUser().getWeight() * 3.5) / 200 * activityTime) + "");
    }

    private void findCalories(Date date) {

        int timeActive = 0;
        int accountSteps = 0;
        List<Optional<Steps>> list = stepsDatabaseHelper.get(getModel().getUser().getUserID(), date);
        for (int i = 0; i < list.size(); i++) {
            Steps steps = list.get(i).get();
            timeActive = steps.getDailyActiveTime();
            accountSteps = steps.getDailySteps();
        }

        DecimalFormat df = new DecimalFormat("######0.00");
        String calories = (2.0 * 3.5 * getModel().getUser().getWeight()) / 200 * timeActive + "";
        caloriesTextView.setText(df.format(Double.parseDouble(calories)));


        String stepsKm = (getModel().getUser().getHeight() * 0.45) / 100 * accountSteps / 1000 + "";
        kmTextView.setText(df.format(Double.parseDouble(stepsKm)));
        activeTimeTextView.setText(formatTimeActivity(timeActive));

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
        String laftdate = simple.format(leftMouth);
        mTitleCalendarTextView.setText(laftdate.split("-")[2] + " " + new SimpleDateFormat("MMM", Locale.US).format(leftMouth));
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
                drawGraph(true);
                findCalories(downDate);
                List<Optional<Steps>> stepsList = getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(), selectedDate);
                if (stepsList.isEmpty()) {
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        EventBus.getDefault().register(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    /**
     * @param event every 10s, do little sync
     */
    @Subscribe
    public void onEvent(final LittleSyncEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
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
                    drawGraph(true);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(BLEBluetoothOffEvent event) {
        showStateString(R.string.in_app_notification_bluetooth_disabled);
    }

    @Subscribe
    public void onEvent(BLEConnectionStateChangedEvent event) {
        if (event.isConnected()) {
            showStateString(R.string.in_app_notification_found_watch);
        } else {
            showStateString(R.string.in_app_notification_watch_disconnected);
        }
    }

    @Subscribe
    public void onEvent(final BLESearchEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getSearchEvent() == BLESearchEvent.SEARCH_EVENT.ON_SEARCHING) {
                    PermissionRequestDialogBuilder builder = new PermissionRequestDialogBuilder(ActivitiesActivity.this);
                    builder.addPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                    builder.askForPermission(ActivitiesActivity.this, 1);
                    showStateString(R.string.in_app_notification_searching);
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

    private void showStateString(int resId) {
        Snackbar snackbar = Snackbar.make(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), "", Snackbar.LENGTH_LONG);
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setText(getString(resId));
        snackbar.show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
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