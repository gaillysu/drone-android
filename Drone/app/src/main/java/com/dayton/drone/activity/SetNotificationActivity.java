package com.dayton.drone.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.SetNotificationContactsAdapter;
import com.dayton.drone.model.Contact;
import com.dayton.drone.model.Notification;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 16/5/18.
 */
public class SetNotificationActivity extends BaseActivity {
    private final String CONTACTSKEY = "contacts";
    @Bind(R.id.activity_set_notification_contacts_listview)
    ListView contactsListView;
    List<Contact> contactsList;
    SetNotificationContactsAdapter setNotificationContactsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        ButterKnife.bind(this);
        contactsList = new ArrayList<>();
        List<Notification> notifications = getModel().getNotificationDatabaseHelper().get("unknown");
        if(!notifications.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONObject(notifications.get(0).getContactsList()).getJSONArray(CONTACTSKEY);
                for(int i=0;i<jsonArray.length();i++)
                {
                    contactsList.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(),Contact.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setNotificationContactsAdapter = new SetNotificationContactsAdapter(this,R.layout.set_notification_contacts_list_item, contactsList);
        contactsListView.setAdapter(setNotificationContactsAdapter);
    }

    @OnClick(R.id.activity_set_notification_back_imagebutton)
    public void back()
    {
        finish();
    }

    @OnClick(R.id.activity_set_notification_add_imagebutton)
    public void addContact()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        SetNotificationActivity.this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData,null,null,null,null);
            cursor.moveToFirst();
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactNumber = "";

            int phoneNum = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (phoneNum > 0)
            {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phonesCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId,
                        null, null);
                if(phonesCursor.getCount()>0)                {
                    phonesCursor.moveToFirst();
                    for (;!phonesCursor.isAfterLast();phonesCursor.moveToNext())
                    {
                        int index = phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        contactNumber = phonesCursor.getString(index);
                        break;
                    }
                    if (!phonesCursor.isClosed())
                    {
                        phonesCursor.close();
                    }
                }
            }
            //finish finding name and phone number
            if(!contactName.isEmpty() && !contactNumber.isEmpty())
            {
                Notification notification = new Notification();
                notification.setApplication("unknown");

                Contact contact = new Contact();
                contact.setName(contactName);
                contact.setNumber(contactNumber);
                contactsList.add(contact);

                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for(Contact contact1:contactsList)
                {
                    try {
                        jsonArray.put(new JSONObject(new Gson().toJson(contact1)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    jsonObject.put(CONTACTSKEY,jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notification.setContactsList(jsonObject.toString());
                getModel().getNotificationDatabaseHelper().update(notification);
                setNotificationContactsAdapter.notifyDataSetChanged();
            }
        }
    }
}
