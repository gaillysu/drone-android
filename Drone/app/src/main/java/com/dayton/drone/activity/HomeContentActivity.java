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
import com.dayton.drone.modle.Steps;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.view.CalendarView;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
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
public class HomeContentActivity extends BaseActivity implements OnChartValueSelectedListener {
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
    @Bind(R.id.fragment_home_title_guide_dec)
    LinearLayout titleDec;

    @Bind(R.id.fragment_home_content_bar)
    BarChart barChart;

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
        setContentView(R.layout.fragment_home_content);
        ButterKnife.bind(this);

        titleView = findViewById(R.id.fragment_title);
        mIvNextMouth = (ImageButton) findViewById(R.id.home_fragmet_title_next_day);
        mIvBackMouth = (ImageButton) findViewById(R.id.home_fragment_calendar_back_day);
        mBtBack = (Button)findViewById(R.id.home_fragment_title_back);
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
        initData();
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
                            barChart.setBackgroundResource(R.drawable.user_guide_bg);
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
        }else{
            guideView.setVisibility(View.GONE);
        }
    }




    public void initData() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse("2015-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setCalendarData(date);
        String mouth = calendar.getYearAndmonth();
        mTitleCalendarTextView.setText(getResources().
                getString(R.string.main_table_date) + " " + mouth);


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
                mTitleCalendarTextView.setText(getString(R.string.main_table_date)
                        + " " + leftMouth);
                mIvNextMouth.setVisibility(View.GONE);
                mIvBackMouth.setVisibility(View.GONE);
            }
        });

        mIvNextMouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rightMouth = calendar.clickRightMonth();
                mTitleCalendarTextView.setText(getResources().getString(R.string.main_table_date)
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
        barChart.highlightValue(e.getXIndex(), dataSetIndex);
    }

    @Override
    public void onNothingSelected() {

    }
}
