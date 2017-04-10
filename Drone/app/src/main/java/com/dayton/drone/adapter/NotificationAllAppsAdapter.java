package com.dayton.drone.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.ble.notification.PackageFilterHelper;
import com.dayton.drone.event.NotificationPackagesChangedEvent;
import com.dayton.drone.model.NotificationModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Jason on 2016/11/21.
 */

public class NotificationAllAppsAdapter extends BaseAdapter {

    private List<NotificationModel> notificationApps;
    private Context context;

    public NotificationAllAppsAdapter(Context context, List<NotificationModel> notificationBean) {
        this.notificationApps = notificationBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notificationApps.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.notification_adapter_item,null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.notification_app_icon);
            viewHolder.table = (TextView) convertView.findViewById(R.id.notification_app_name);
            viewHolder.mSwitch = (SwitchCompat) convertView.findViewById(R.id.notification_is_on_off);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final NotificationModel bean = notificationApps.get(position);
        if (bean != null) {
            viewHolder.appIcon.setImageDrawable(ContextCompat.getDrawable(context, bean.getImageResource()));
            viewHolder.table.setText(bean.getNameStringResource());
            viewHolder.mSwitch.setChecked(bean.getSwitchSign());
            viewHolder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(bean.getNameStringResource()==R.string.notification_call_title) {
                        PackageFilterHelper.setCallFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_facebook_messenger_title) {
                        PackageFilterHelper.setMessengerFacebookFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_message_title) {
                        PackageFilterHelper.setSmsFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_gmail_inbox_title) {
                        PackageFilterHelper.setGmailFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_email_title) {
                        PackageFilterHelper.setEmailFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_telegram_messenger_title) {
                        PackageFilterHelper.setMessengerTelegramFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_calendar_title) {
                        PackageFilterHelper.setCalendarFilterEnable(context, isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_facebook_title) {
                        PackageFilterHelper.setFacebookFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_googleplus_title) {
                        PackageFilterHelper.setGooglePlusFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_instagram_title) {
                        PackageFilterHelper.setInstagramFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_snapchat_title) {
                        PackageFilterHelper.setSnapchatFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_linkedin_title) {
                        PackageFilterHelper.setLinkedinFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_twitter_title) {
                        PackageFilterHelper.setTwitterFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_wechat_title) {
                        PackageFilterHelper.setWechatFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_whatsapp_title) {
                        PackageFilterHelper.setWhatsappFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_qq_title) {
                        PackageFilterHelper.setQQFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_skype_title) {
                        PackageFilterHelper.setSkypeFilterEnable(context,isChecked);
                    }
                    else if(bean.getNameStringResource()==R.string.notification_outlook_title) {
                        PackageFilterHelper.setOutlookFilterEnable(context,isChecked);
                    }
                    EventBus.getDefault().post(new NotificationPackagesChangedEvent());
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView appIcon;
        TextView table;
        SwitchCompat mSwitch;
    }

}
