package com.dayton.drone.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dayton.drone.utils.Constance;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.UIUtils;
import com.dayton.drone.view.CalendarView;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.dayton.drone.R.id.home_fragmet_title_next_day;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentActivity extends BaseActivity {
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

    @Bind(R.id.home_content_bar)
    RelativeLayout guideBar;
    @Bind(R.id.fragment_home_title_guide_dec)
    LinearLayout titleDec;

    @Bind(R.id.home_content_histogram_ll)
    LinearLayout histogramView;
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
    private Button mIvBack;
    private ImageButton mIvBackDay;
    private ImageButton mIvNextDay;
    private LinearLayout showCalendar;
    private SimpleDateFormat format;
    private PopupWindow popupWindow;
    private TextView mTitleCalendarTextView;
    private CalendarView calendar;
    private View calendarView;

    private int guidePage = 1;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_content);
    }

    public void initview(LayoutInflater inflater, ViewGroup container) {

       titleView =findViewById(R.id.fragment_title);
        mIvNextDay = (ImageButton) findViewById(home_fragmet_title_next_day);
        mIvBackDay = (ImageButton)findViewById(home_fragmet_title_next_day);
        mIvBack = (Button) titleView.findViewById(R.id.home_fragment_title_back);
        showCalendar = (LinearLayout) findViewById(R.id.home_fragment_title_date);
        dateTv = (LinearLayout) findViewById(R.id.home_fragment_title_date);
        mTitleCalendarTextView = (TextView) findViewById(R.id.home_fragment_title_date_tv);
        calendarView = View.inflate(getModel(), R.layout.date_layout_popupwindow, null);

        ButterKnife.bind(this);
        mContext = UIUtils.getContext();
        mIsFirst = SpUtils.getBoolean(mContext, Constance.IS_FIRST, true);

        mProgressBar.setStartColor(R.color.progress_start_color);
        mProgressBar.setEndColor(R.color.progress_end_color);
        mProgressBar.setSmoothPercent(0.3f);
        homeMiddleTv.setText(200 + "");
        startView();
    }

    private void startView() {
        if (mIsFirst) {
            dateTv.setBackgroundResource(R.drawable.user_guide_bg);
            titleGuide.setVisibility(View.VISIBLE);
            guideView.setVisibility(View.VISIBLE);
            guideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (guidePage) {
                        case 1:
                            dateTv.setBackgroundColor(0x80000000);
                            acvitiesGuide.setVisibility(View.VISIBLE);
                            titleGuide.setVisibility(View.GONE);
                            titleDec.setBackgroundResource(R.drawable.user_guide_bg);
                            break;
                        case 2:
                            titleView.setBackgroundColor(0x80000000);
                            guideBar.setBackgroundResource(R.drawable.user_guide_bg);
                            acvitiesGuide.setVisibility(View.GONE);
                            barGuide.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            barGuide.setVisibility(View.GONE);
                            guideBar.setBackgroundColor(0x80000000);
                            chartGuideDec.setVisibility(View.VISIBLE);
                            histogramView.setBackgroundResource(R.drawable.user_guide_bg);
                            break;
                        case 4:
                            chartGuideDec.setVisibility(View.GONE);
                            histogramView.setBackgroundColor(0x80000000);
                            guideView.setVisibility(View.GONE);
                            break;

                    }
                    guidePage++;
                }
            });
        }
    }

    protected void initData() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse("2015-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setCalendarData(date);
        String ya = calendar.getYearAndmonth();
        mTitleCalendarTextView.setText(getResources().
                getString(R.string.main_table_date) + " " + ya);
    }


    public void addListener() {

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvBackDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leftday = calendar.clickLeftDay();
                mTitleCalendarTextView.setText(UIUtils.getString(R.string.main_table_date)
                        + " " + leftday);
            }
        });

        mIvNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rightday = calendar.clickRightDay();
                mTitleCalendarTextView.setText(UIUtils.getString(R.string.main_table_date)
                        + " " + rightday);
            }
        });

        showCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager manager = getWindowManager();
                Display display = manager.getDefaultDisplay();
                popupWindow = new PopupWindow(calendarView,display.getWidth(),display.getHeight());
                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(titleView);

            }
        });

        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                String[] dateArray = format.format(downDate).split("-");
                mTitleCalendarTextView.setText(UIUtils.getString
                        (R.string.main_table_date) + " " + dateArray[2]);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(popupWindow != null) {
                popupWindow.dismiss();
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
}
