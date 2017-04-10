package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.NotificationAllAppsAdapter;
import com.dayton.drone.ble.notification.PackageFilterHelper;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2016/11/21.
 */

public class NewSetNotificationActivity extends BaseActivity{

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

        adapter = new NotificationAllAppsAdapter(this, notificationBean);
        allNotificationApps.setAdapter(adapter);
    }

    @OnClick(R.id.activity_set_notification_back_imagebutton)
    public void backUp() {
        finish();
    }

}
