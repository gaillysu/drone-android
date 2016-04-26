package com.dayton.drone.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.view.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentFragment extends BaseFragment implements View.OnClickListener {


    private ImageView mIvBack, mIvMonthBack, mIvMonthNext, mIvNext;
    private LinearLayout mShowDate;
    private CalendarView calendar;
    private TextView calendarCenter;
    private Context mContext;

    private View viewPopupWindow;
    private View tableView;
    private SimpleDateFormat format;
    private String userSelectDate;
    private PopupWindow datePopupWindow;


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_home_content, null);
        mContext = getActivity();
        ButterKnife.bind((Activity) mContext);

        tableView = view.findViewById(R.id.table);
        mIvBack = (ImageView) view.findViewById(R.id.back_page);
        mIvMonthBack = (ImageView) view.findViewById(R.id.back_month);
        mShowDate = (LinearLayout) view.findViewById(R.id.table_date);
        mIvMonthNext = (ImageView) view.findViewById(R.id.next_month);
        mIvNext = (ImageView) view.findViewById(R.id.table_next_page);
        calendarCenter = (TextView) view.findViewById(R.id.table_date_tv);
        mIvBack.setVisibility(View.GONE);
        mIvMonthBack.setVisibility(View.GONE);
        mIvMonthNext.setVisibility(View.GONE);
        mIvNext.setVisibility(View.GONE);

        initPopupWindow();
        addListener();
        return view;
    }

    private void initPopupWindow() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        viewPopupWindow = View.inflate(mContext, R.layout.date_layout_popupwindow, null);
        calendar = (CalendarView) viewPopupWindow.findViewById(R.id.calendar_popupwindow_layout);
        calendar.setSelectMore(false);

        Date date = null;
        try {
            date = format.parse("2015-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setCalendarData(date);
    }


    private void addListener() {
        mShowDate.setOnClickListener(this);
        mIvMonthBack.setOnClickListener(this);
        mIvMonthNext.setOnClickListener(this);
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                userSelectDate = format.format(downDate);
            }
        });
    }

    @Override
    protected void initData() {
        String ya = calendar.getYearAndmonth();
        calendarCenter.setText(getResources().getString(R.string.main_table_date) + " " + ya);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_date:
                datePopupWindow = new PopupWindow(viewPopupWindow, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mIvMonthNext.setVisibility(View.VISIBLE);
                mIvMonthBack.setVisibility(View.VISIBLE);
                datePopupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
                datePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mIvMonthBack.setVisibility(View.GONE);
                        mIvMonthNext.setVisibility(View.GONE);
                    }
                });
                datePopupWindow.showAsDropDown(tableView, +tableView.getHeight(), 0);
                break;
            case R.id.back_month:
                String leftYearAndmonth = calendar.clickLeftMonth();
                calendarCenter.setText(getResources().getString(R.string.main_table_date) + " " + leftYearAndmonth);
                break;
            case R.id.next_month:
                String rightYearAndmonth = calendar.clickRightMonth();
                calendarCenter.setText(getResources().getString(R.string.main_table_date) + " " + rightYearAndmonth);
                break;
        }
    }


}
