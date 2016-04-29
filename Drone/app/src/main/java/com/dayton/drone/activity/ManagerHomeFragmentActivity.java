package com.dayton.drone.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.fragment.GalleryFragment;
import com.dayton.drone.fragment.HomeContentFragment;
import com.dayton.drone.fragment.SleepFragment;
import com.dayton.drone.fragment.WatchSettingFragment;
import com.dayton.drone.fragment.WorldClockFragment;
import com.dayton.drone.utils.UIUtils;
import com.dayton.drone.view.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

import static com.dayton.drone.R.id.home_fragmet_title_next_day;

/**
 * Created by boy on 2016/4/28.
 */
public class ManagerHomeFragmentActivity extends BaseActivity {
    private SimpleDateFormat format;

    private FrameLayout frameLayout;
    private View calendarView;
    private RelativeLayout titleView;
    private CalendarView calendar;
    private LinearLayout showCalendar;
    private PopupWindow popupWindow;

    private TextView mTitleCalendarTextView;
    private Button mIvBack;

    private ImageButton mIvBackDay;

    private ImageButton mIvNextDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);
        ButterKnife.bind(this);
        initView();
        initData();
        addListener();
        selectFragment();

    }

    private void selectFragment() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        switch (type) {
            case 1:
                HomeContentFragment homeFragment = new HomeContentFragment();
                loadFragment(homeFragment);
                break;
            case 2:
                SleepFragment po = new SleepFragment();
                loadFragment(po);
                break;
            case 3:
                WorldClockFragment clockFragment = new WorldClockFragment();
                loadFragment(clockFragment);
                break;
            case 4:
                WatchSettingFragment setting = new WatchSettingFragment();
                loadFragment(setting);
                break;
            case 5:
                GalleryFragment al = new GalleryFragment();
                loadFragment(al);
                break;
        }
    }

    private void initView() {

        mIvNextDay = (ImageButton) findViewById(home_fragmet_title_next_day);
        mIvBackDay = (ImageButton) findViewById(home_fragmet_title_next_day);
        mIvBack = (Button) findViewById(R.id.home_fragment_title_back);
        titleView = (RelativeLayout) findViewById(R.id.fragment_title);
        mTitleCalendarTextView = (TextView) findViewById(R.id.home_fragment_title_date_tv);
        frameLayout = (FrameLayout) findViewById(R.id.manager_fragment_framelayout);
        mIvNextDay = (ImageButton) findViewById(home_fragmet_title_next_day);
        mIvBackDay = (ImageButton) findViewById(home_fragmet_title_next_day);
        showCalendar = (LinearLayout) findViewById(R.id.home_fragment_title_date);
        calendarView = View.inflate(getModel(), R.layout.date_layout_popupwindow, null);
        calendar = (CalendarView) calendarView.findViewById(R.id.calendar_popupwindow_layout);
        calendar.setSelectMore(false);
    }

    private void initData() {
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
                popupWindow = new PopupWindow(calendarView, ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
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
            popupWindow.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadFragment(Fragment al) {
        getFragmentManager().beginTransaction()
                .replace(R.id.manager_fragment_framelayout, al).commit();

    }
}
