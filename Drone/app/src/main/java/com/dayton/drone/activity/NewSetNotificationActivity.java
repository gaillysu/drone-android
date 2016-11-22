package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.NotificationAllAppsAdapter;
import com.dayton.drone.model.CalendarNotification;
import com.dayton.drone.model.EmailNotification;
import com.dayton.drone.model.FacebookNotification;
import com.dayton.drone.model.MessageNotification;
import com.dayton.drone.model.NotificationModel;
import com.dayton.drone.model.QQNotification;
import com.dayton.drone.model.TelephoneNotification;
import com.dayton.drone.model.TwitterNotification;
import com.dayton.drone.model.WeChatNotification;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2016/11/21.
 */

public class NewSetNotificationActivity extends BaseActivity {

    @Bind(R.id.notification_all_app_listView)
    ListView allNotificationApps;

    private NotificationAllAppsAdapter adapter;
    private List<NotificationModel> notificationBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_notification_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        notificationBean = new ArrayList<>();
        TelephoneNotification telephone = new TelephoneNotification();
        notificationBean.add(telephone);
        MessageNotification message = new MessageNotification();
        notificationBean.add(message);
        EmailNotification email = new EmailNotification();
        notificationBean.add(email);
        CalendarNotification calendar = new CalendarNotification();
        notificationBean.add(calendar);
        FacebookNotification facebook = new FacebookNotification();
        notificationBean.add(facebook);
        TwitterNotification twitter = new TwitterNotification();
        notificationBean.add(twitter);
        QQNotification qq = new QQNotification();
        notificationBean.add(qq);
        WeChatNotification weChat = new WeChatNotification();
        notificationBean.add(weChat);
        adapter = new NotificationAllAppsAdapter(this, notificationBean);
        allNotificationApps.setAdapter(adapter);
    }

    @OnClick(R.id.activity_set_notification_back_imagebutton)
    public void backUp() {
        finish();
    }
}
