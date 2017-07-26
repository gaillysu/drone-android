package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.NotificationAllAppsAdapter;
import com.dayton.drone.ble.notification.PackageFilterHelper;
import com.dayton.drone.event.NotificationPackagesChangedEvent;
import com.dayton.drone.model.CalendarNotification;
import com.dayton.drone.model.EmailNotification;
import com.dayton.drone.model.FacebookMessengerNotification;
import com.dayton.drone.model.FacebookNotification;
import com.dayton.drone.model.GmailNotification;
import com.dayton.drone.model.GooglePlusNotification;
import com.dayton.drone.model.InstagramNotification;
import com.dayton.drone.model.LinkedinNotification;
import com.dayton.drone.model.MessageNotification;
import com.dayton.drone.model.NotificationModel;
import com.dayton.drone.model.OutlookNotification;
import com.dayton.drone.model.QQNotification;
import com.dayton.drone.model.SkypeNotification;
import com.dayton.drone.model.SnapchatNotification;
import com.dayton.drone.model.TelegramMessengerNotification;
import com.dayton.drone.model.TelephoneNotification;
import com.dayton.drone.model.TwitterNotification;
import com.dayton.drone.model.WeChatNotification;
import com.dayton.drone.model.WhatsappNotification;
import com.dayton.drone.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2016/11/21.
 */

public class NewSetNotificationActivity extends BaseActivity{

    @Bind(R.id.notification_all_app_listView)
    ListView allAppListView;
    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;
    SwitchCompat enableAllAppSwitch;
    private NotificationAllAppsAdapter adapter;
    private List<NotificationModel> notificationBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_notification_activity);
        ButterKnife.bind(this);
        initView();
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        title.setText(getString(R.string.notifications_notifications));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        View headerView = getLayoutInflater().inflate(R.layout.notification_all_header, null);
        enableAllAppSwitch = (SwitchCompat) headerView.findViewById(R.id.notification_all_app_switch);
        enableAllAppSwitch.setChecked(PackageFilterHelper.getAllApplicationsFilterEnable(this));
        enableAllAppSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PackageFilterHelper.setAllApplicationsFilterEnable(NewSetNotificationActivity.this,isChecked);
                initNotificationBean();
                adapter.setNotificationApps(notificationBean);
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new NotificationPackagesChangedEvent());
            }
        });
        allAppListView.addHeaderView(headerView);
        initNotificationBean();
        adapter = new NotificationAllAppsAdapter(NewSetNotificationActivity.this, notificationBean);
        allAppListView.setAdapter(adapter);
    }

    private void initNotificationBean() {
        notificationBean = new ArrayList<>();
        notificationBean.add(new TelephoneNotification(PackageFilterHelper.getCallFilterEnable(this)));
        notificationBean.add(new FacebookMessengerNotification(PackageFilterHelper.getMessengerFacebookFilterEnable(this)));
        notificationBean.add(new MessageNotification(PackageFilterHelper.getSmsFilterEnable(this)));
        notificationBean.add(new GmailNotification(PackageFilterHelper.getGmailFilterEnable(this)));
        notificationBean.add(new TelegramMessengerNotification(PackageFilterHelper.getMessengerTelegramFilterEnable(this)));
        notificationBean.add(new CalendarNotification(PackageFilterHelper.getCalendarFilterEnable(this)));
        //we use a social app name to below apps: facebook,twitter,qq,wechat,whatsapp,linkedin,instagram...
        notificationBean.add(new FacebookNotification(PackageFilterHelper.getFacebookFilterEnable(this)));
        notificationBean.add(new GooglePlusNotification(PackageFilterHelper.getGooglePlusFilterEnable(this)));
        notificationBean.add(new InstagramNotification(PackageFilterHelper.getInstagramFilterEnable(this)));
        notificationBean.add(new SnapchatNotification(PackageFilterHelper.getSnapchatFilterEnable(this)));
        notificationBean.add(new LinkedinNotification(PackageFilterHelper.getLinkedinFilterEnable(this)));
        notificationBean.add(new TwitterNotification(PackageFilterHelper.getTwitterFilterEnable(this)));
        notificationBean.add(new WeChatNotification(PackageFilterHelper.getWechatFilterEnable(this)));
        notificationBean.add(new WhatsappNotification(PackageFilterHelper.getWhatsappFilterEnable(this)));
        notificationBean.add(new QQNotification(PackageFilterHelper.getQQFilterEnable(this)));
        notificationBean.add(new SkypeNotification(PackageFilterHelper.getSkypeFilterEnable(this)));
        notificationBean.add(new EmailNotification(PackageFilterHelper.getEmailFilterEnable(this)));
        notificationBean.add(new OutlookNotification(PackageFilterHelper.getOutlookFilterEnable(this)));
    }
}
