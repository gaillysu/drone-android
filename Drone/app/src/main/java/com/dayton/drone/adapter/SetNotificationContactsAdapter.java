package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.model.Contact;

import java.util.List;

/**
 * Created by med on 16/5/18.
 */
public class SetNotificationContactsAdapter extends ArrayAdapter<Contact> {
    Context context;
    int resource;
    List<Contact> contactsList;
    public SetNotificationContactsAdapter(Context context, int resource,List<Contact> contactsList) {
        super(context, resource,contactsList);
        this.context = context;
        this.resource = resource;
        this.contactsList = contactsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = View.inflate(context,resource,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.activity_set_notification_contacts_name_textview);
        if(contactsList.get(position)!=null && contactsList.get(position).getName()!=null)
        {
            textView.setText(contactsList.get(position).getName());
        }
        return convertView;
    }
}
