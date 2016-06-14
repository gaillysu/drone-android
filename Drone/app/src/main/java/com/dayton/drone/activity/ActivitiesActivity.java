package com.dayton.drone.activity;


import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.model.Steps;
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
    private StepsDatabaseHelper stepsDatabaseHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);//通知栏所需颜色
        }

        ButterKnife.bind(this);

        calendarGroup.setVisibility(View.GONE);
        boolean mIsFirst = SpUtils.getBoolean(this, CacheConstants.IS_FIRST, true);
        mProgressBar.setStartColor(R.color.progress_start_color);
        mProgressBar.setEndColor(R.color.progress_end_color);
        mProgressBar.setSmoothPercent(1.0f * SpUtils.getIntMethod(this, CacheConstants.TODAY_STEP, 0)
                / SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000)+0.5f);
        homeMiddleTv.setText(SpUtils.getIntMethod(this, CacheConstants.TODAY_STEP, 0) + "");
        userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal)
                + SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000));
        calendar.setSelectMore(false);
        nextMonth.setVisibility(View.GONE);
        backMonth.setVisibility(View.GONE);
        stepsDatabaseHelper = getModel().getStepsDatabaseHelper();
        Date date = new Date(System.currentTimeMillis());
        findCalories(date);


        modifyChart(hourlyBarChart);
        modifyChart(lastMonthLineChart);
        modifyChart(lastWeekLineChart);
        modifyChart(thisWeekLineChart);

        drawGraph();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void findCalories(Date date) {

        Long timeActive = 0l;
        int accountSteps=0;
        List<Optional<Steps>> list = stepsDatabaseHelper.get(getModel().getUser().getUserID(),date);
        for(int i = 0; i <list.size();i++){
            Steps steps = list.get(i).get();
            timeActive =  steps.getTimeFrame()/(60*1000);
            accountSteps = steps.getDailySteps();
        }
        double calories = (2.0*3.5*getModel().getUser().getWeight())/200*timeActive;
        caloriesTextView.setText(calories+"");
        kmTextView.setText((getModel().getUser().getHeight()*0.45)/100*accountSteps/1000+"");
        activeTimeTextView.setText(timeActive+"");

    }

    private void drawGraph() {
        StepsHandler stepsHandler = new StepsHandler(getModel().getStepsDatabaseHelper(), getModel().getUser());
        setDataInChart(hourlyBarChart, stepsHandler.getDailySteps(selectedDate));
        setDataInChart(thisWeekLineChart, stepsHandler.getThisWeekSteps(selectedDate));
        setDataInChart(lastWeekLineChart, stepsHandler.getLastWeekSteps(selectedDate));
        setDataInChart(lastMonthLineChart, stepsHandler.getLastMonthSteps(selectedDate));
    }

    private void setDataInChart(BarChart barChart, DailySteps dailySteps) {
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();
        for (int i = 0; i < dailySteps.getHourlySteps().length; i++) {
            yValue.add(new BarEntry(dailySteps.getHourlySteps()[i], i));
            xVals.add(i + ":00");
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

    private void modifyChart(BarChart barChart) {
        calendar.setCalendarData(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String day = dateFormat.format(new Date());
        mTitleCalendarTextView.setText(new SimpleDateFormat("MMM", Locale.US).format(selectedDate) + day.split("-")[2]);
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

    private void modifyChart(LineChart lineChart) {
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
    public void guideViewClick() {
        ++guidePage;
        switch (guidePage) {
            case 2:
                titleDec.setBackgroundResource(R.drawable.user_guide_bg);
                showCalendar.setBackground(new BitmapDrawable());
                activitiesGuide.setVisibility(View.VISIBLE);
                titleGuide.setVisibility(View.GONE);
                break;
            case 3:
                titleDec.setBackground(new BitmapDrawable());
                guideBar.setBackgroundResource(R.drawable.user_guide_bg);
                activitiesGuide.setVisibility(View.GONE);
                barGuide.setVisibility(View.VISIBLE);
                break;
            case 4:
                barGuide.setVisibility(View.GONE);
                hourlyBarChart.setBackgroundResource(R.drawable.user_guide_bg);
                guideBar.setBackground(new BitmapDrawable ());
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
    public void mIvBackMonthClick() {
        Date leftMouth = calendar.clickLeftMonth();
//        mTitleCalendarTextView.setText(new SimpleDateFormat("MMM").format(leftMouth));
    }

    @OnClick(R.id.activities_activity_title_next_month)
    public void mIvNextMonthClick() {
        Date rightMouth = calendar.clickRightMonth();
//        mTitleCalendarTextView.setText(new SimpleDateFormat("MMM").format(rightMouth));
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
                mTitleCalendarTextView.setText(new SimpleDateFormat("MMM", Locale.US)
                        .format(downDate) + new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(downDate).split("-")[2]);
                drawGraph();
                findCalories(downDate);
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

    @Subscribe
    public void onEvent(final LittleSyncEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                homeMiddleTv.setText(event.getSteps() + "");
                userStepGoalTextView.setText(getResources().getString(R.string.user_step_goal)
                        + event.getGoal());
                mProgressBar.setSmoothPercent(1.0f * event.getSteps() / event.getGoal());
                SpUtils.putIntMethod(getApplicationContext(), CacheConstants.GOAL_STEP, event.getGoal());
                SpUtils.putIntMethod(getApplicationContext(), CacheConstants.TODAY_STEP, event.getSteps());
            }
        });
    }

    @Subscribe
    public void onEvent(final BigSyncEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getStatus() == BigSyncEvent.BIG_SYNC_EVENT.STOPPED) {
                    drawGraph();
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