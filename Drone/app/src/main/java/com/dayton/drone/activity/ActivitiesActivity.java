package com.dayton.drone.activity;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import net.medcorp.library.ble.util.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/22.
 */
public class ActivitiesActivity extends BaseActivity implements OnChartValueSelectedListener {
    private boolean mIsFirst = true;
    private Context mContext;
    @Bind(R.id.home_fragment_progress_bar)
    MagicProgressCircle mProgressBar;
    @Bind(R.id.home_fragment_progress_middle_tv)
    TextView homeMiddleTv;

    @Bind(R.id.fragment_home_title_calories)
    TextView caloriesTextView;
    @Bind(R.id.fragment_home_title_miles)
    TextView milesTextView;
    @Bind(R.id.fragment_home_title_active_time)
    TextView activeTimeTextView;

    @Bind(R.id.home_content_bar)
    RelativeLayout guideBar;
    @Bind(R.id.hourly_header_layout_guide_layout)
    LinearLayout titleDec;

    @Bind(R.id.activity_activities_hourly_bar)
    BarChart hourlyBarChart;

    @Bind(R.id.activity_activities_weekly_line)
    LineChart thisweekLineChart;

    @Bind(R.id.activity_activities_last_weekly_line)
    LineChart lastweekLineChart;

    @Bind(R.id.activity_activities_monthly_line)
    LineChart lastmonthLineChart;

    @Bind(R.id.fragment_home_guide_view)
    RelativeLayout guideView;
    @Bind(R.id.home_fragement_guide_title_dec)
    TextView titleGuide;
    @Bind(R.id.home_fragement_guide_dec_actvies)
    TextView acvitiesGuide;
    @Bind(R.id.home_fragement_guide_dec_bar)
    TextView barGuide;
    @Bind(R.id.home_fragement_guide_dec_chart)
    TextView chartGuideDec;

    private LinearLayout dateTv;
    private View titleView;
    private Button mBtBack;
    private ImageButton mIvBackMouth;
    private ImageButton mIvNextMouth;
    private LinearLayout showCalendar;
    private SimpleDateFormat format;
    private PopupWindow popupWindow;
    private TextView mTitleCalendarTextView;
    private CalendarView calendar;
    private View calendarView;

    private int guidePage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        ButterKnife.bind(this);

        titleView = findViewById(R.id.fragment_title);
        mIvNextMouth = (ImageButton) findViewById(R.id.home_fragmet_title_next_day);
        mIvBackMouth = (ImageButton) findViewById(R.id.home_fragment_calendar_back_day);
        mBtBack = (Button) findViewById(R.id.home_fragment_title_back);
        showCalendar = (LinearLayout) findViewById(R.id.home_fragment_title_date);
        dateTv = (LinearLayout) findViewById(R.id.home_fragment_title_date);
        mTitleCalendarTextView = (TextView) findViewById(R.id.home_fragment_title_date_tv);
        calendarView = View.inflate(getModel(), R.layout.date_layout_popupwindow, null);

        mContext = getModel();
        mIsFirst = SpUtils.getBoolean(mContext, CacheConstants.IS_FIRST, true);

        mProgressBar.setStartColor(R.color.progress_start_color);
        mProgressBar.setEndColor(R.color.progress_end_color);
        mProgressBar.setSmoothPercent(0.3f);
        homeMiddleTv.setText(200 + "");
        calendar = (CalendarView) calendarView.findViewById(R.id.calendar_popupwindow_layout);
        calendar.setSelectMore(false);

        mIvNextMouth.setVisibility(View.GONE);
        mIvBackMouth.setVisibility(View.GONE);
        initHourlyData();
        initweeklyData();
        initlastweeklyData();
        initmonthlyData();
        startView();
        addListener();
    }

    private void startView() {
        if (mIsFirst) {
            dateTv.setBackgroundResource(R.drawable.user_guide_bg);
            titleGuide.setVisibility(View.VISIBLE);
            guideView.setVisibility(View.VISIBLE);
            guideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ++guidePage;
                    switch (guidePage) {
                        case 2:
                            titleDec.setBackgroundResource(R.drawable.user_guide_bg);
                            dateTv.setBackgroundDrawable(new BitmapDrawable());
                            acvitiesGuide.setVisibility(View.VISIBLE);
                            titleGuide.setVisibility(View.GONE);

                            break;
                        case 3:

                            titleDec.setBackgroundDrawable(new BitmapDrawable());
                            guideBar.setBackgroundResource(R.drawable.user_guide_bg);
                            acvitiesGuide.setVisibility(View.GONE);
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
            });
        } else {
            guideView.setVisibility(View.GONE);
        }
    }


    public void initHourlyData() {
        calendar.setCalendarData(new Date());
        mTitleCalendarTextView.setText(new SimpleDateFormat("MMM").format(new Date()));
        hourlyBarChart.setDescription("");
        hourlyBarChart.getLegend().setEnabled(false);
        hourlyBarChart.setOnChartValueSelectedListener(this);
        hourlyBarChart.setPinchZoom(false);
        hourlyBarChart.setDrawGridBackground(false);
        hourlyBarChart.setScaleEnabled(false);
        hourlyBarChart.setDrawValueAboveBar(false);
        hourlyBarChart.setDoubleTapToZoomEnabled(false);
        hourlyBarChart.setDragEnabled(true);

        YAxis leftAxis = hourlyBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setLabelCount(3, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new YAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                int resValue = (int) value;
                return resValue+"";
            }
        });
        YAxis rightAxis = hourlyBarChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = hourlyBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);

        SimpleDateFormat sdf = new SimpleDateFormat("HH: mm");
        List<String> xVals = new ArrayList<String>();
        List<BarEntry> yValue = new ArrayList<BarEntry>();

        List<Optional<Steps>> stepsList = getModel().getStepsDatabaseHelper().getAll(getModel().getUser().getUserID());

        int i = 0;
        for (Optional<Steps> steps : stepsList) {
            //TODO here use sample data
            if(steps.get().getSteps()==0) {steps.get().setSteps(new Random().nextInt(10000));}
            if(steps.get().getSteps()>0)
            {
                yValue.add(new BarEntry(new float[]{steps.get().getSteps()}, i));
                xVals.add(sdf.format(new Date(steps.get().getTimeFrame())));
                i++;
            }
        }

        if (stepsList.size() < 24) {
            hourlyBarChart.setScaleMinima((.14f), 1f);
        }else{
            hourlyBarChart.setScaleMinima((stepsList.size()/24f),1f);
        }

        BarDataSet dataSet = new BarDataSet(yValue, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{getResources().getColor(R.color.colorPrimaryDark)});
        List<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(dataSet);
        BarData data = new BarData(xVals, dataSets);
        hourlyBarChart.setData(data);
    }

    void initweeklyData()
    {
        thisweekLineChart.setDescription("");
        thisweekLineChart.getLegend().setEnabled(false);
        thisweekLineChart.setDrawBorders(false);
        thisweekLineChart.setTouchEnabled(false);
        thisweekLineChart.setDragEnabled(false);
        thisweekLineChart.setGridBackgroundColor(Color.WHITE);

        LimitLine ll1 = new LimitLine(7000f, "Goal");
        ll1.setLineWidth(0.5f);
        ll1.setLineColor(Color.BLACK);
        //ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll1.setTextSize(16f);
        ll1.setTextColor(Color.BLACK);

        YAxis leftAxis = thisweekLineChart.getAxisLeft();
        leftAxis.addLimitLine(ll1);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = thisweekLineChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = thisweekLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);

        SimpleDateFormat sdf = new SimpleDateFormat("d'/'M");
        List<String> xVals = new ArrayList<String>();
        List<Entry> yValue = new ArrayList<Entry>();

        List<Optional<Steps>> stepsList = getModel().getStepsDatabaseHelper().getAll(getModel().getUser().getUserID());

        int i = 0;
        for (Optional<Steps> steps : stepsList) {
            //TODO here use sample data
            if(i>6)break;
            if(steps.get().getSteps()==0) {steps.get().setSteps(new Random().nextInt(10000));}
            if(steps.get().getSteps()>0)
            {
                yValue.add(new Entry(steps.get().getSteps(),i));
                xVals.add(sdf.format(new Date(steps.get().getTimeFrame())));
                i++;
            }
        }
        LineDataSet set1 = new LineDataSet(yValue, "");
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setDrawValues(false);
        set1.setDrawCircleHole(false);
        set1.setDrawFilled(true);
        set1.setFillColor(getResources().getColor(R.color.colorPrimaryDark));

        List<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);
        thisweekLineChart.setData(data);
    }
    void initlastweeklyData()
    {

    }
    void initmonthlyData()
    {

    }

    public void addListener() {

        mBtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeActivity.class);
            }
        });
        mIvBackMouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leftMouth = calendar.clickLeftMonth();
                mTitleCalendarTextView.setText(getResources().getString(R.string.main_table_date)
                        + " " + leftMouth);
                mIvNextMouth.setVisibility(View.GONE);
                mIvBackMouth.setVisibility(View.GONE);
            }
        });

        mIvNextMouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rightMouth = calendar.clickRightMonth();
                mTitleCalendarTextView.setText(getString(R.string.main_table_date)
                        + " " + rightMouth);
                mIvNextMouth.setVisibility(View.GONE);
                mIvBackMouth.setVisibility(View.GONE);
            }
        });

        showCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvNextMouth.setVisibility(View.VISIBLE);
                mIvBackMouth.setVisibility(View.VISIBLE);
                WindowManager manager = getWindowManager();
                Display display = manager.getDefaultDisplay();
                popupWindow = new PopupWindow(calendarView, display.getWidth(), display.getHeight());
                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(titleView);

            }
        });

        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                popupWindow.update();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null) {
                popupWindow.dismiss();
                mIvBackMouth.setVisibility(View.GONE);
                mIvNextMouth.setVisibility(View.GONE);
            }
            startActivity(HomeActivity.class);
            return true;
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
