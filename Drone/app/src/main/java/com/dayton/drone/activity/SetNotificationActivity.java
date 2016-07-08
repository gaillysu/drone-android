package com.dayton.drone.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.SetNotificationContactsAdapter;
import com.dayton.drone.ble.util.NotificationPermission;
import com.dayton.drone.model.Contact;
import com.dayton.drone.model.Notification;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 16/5/18.
 */
public class SetNotificationActivity extends BaseActivity {
    private final String CONTACTSKEY = "contacts";
    private final String APPLICATION = "unknown";
    @Bind(R.id.activity_set_notification_contacts_listview)
    SwipeMenuListView contactsListView;
    List<Contact> contactsList;
    SetNotificationContactsAdapter setNotificationContactsAdapter;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }

        ButterKnife.bind(this);
        contactsList = new ArrayList<>();
        List<Notification> notifications = getModel().getNotificationDatabaseHelper().get(APPLICATION);
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

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                deleteItem.setWidth(200);
                deleteItem.setTitleSize(18);
                deleteItem.setTitle("Delete");
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        contactsListView.setMenuCreator(creator);
        contactsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        updateNotification(APPLICATION,contactsList,contactsList.get(position),false);
                        contactsList.remove(position);
                        setNotificationContactsAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        contactsListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        setNotificationContactsAdapter = new SetNotificationContactsAdapter(this,R.layout.set_notification_contacts_list_item, contactsList);
        contactsListView.setAdapter(setNotificationContactsAdapter);
        NotificationPermission.getNotificationAccessPermission(this);
    }

    @OnClick(R.id.activity_set_notification_back_imagebutton)
    public void back()
    {
        finish();
    }


    @OnClick(R.id.activity_set_notification_add_imagebutton)
    public void addContact()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            int hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if(hasReadContactsPermission != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        SetNotificationActivity.this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(contactsList.size()>=10){
            Toast.makeText(this,getString(R.string.add_notification_account_toast),Toast.LENGTH_SHORT).show();
            return;
        }
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
                Contact contact = new Contact();
                contact.setName(contactName);
                //here trim all blank in this number, such as:" 139 1234 5678 " ==> "13912345678"
                //but if user's phonebook save number starts with country code, such as: "+8613923001234", here need remove country code?
                contact.setNumber(contactNumber.replaceAll(" ",""));
                if(!updateNotification(APPLICATION,contactsList,contact,true))
                {
                    Toast.makeText(SetNotificationActivity.this,"Name has got existed",Toast.LENGTH_LONG).show();
                    return;
                }
                contactsList.add(contact);
                setNotificationContactsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    SetNotificationActivity.this.startActivityForResult(intent, 1);
                } else {
                    // Permission Denied
                    Toast.makeText(SetNotificationActivity.this, "Read Contacts Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean updateNotification(String application,List<Contact> contactsList,Contact contact,boolean added)
    {
        Notification notification = new Notification();
        notification.setApplication(application);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Contact contact1:contactsList)
        {
            if( contact.getNumber().equals(contact1.getNumber()) && contact.getName().equals(contact1.getName()))
            {
                if(added)
                {
                    return false;
                }
                else
                {
                    continue;
                }
            }
            try {
                jsonArray.put(new JSONObject(new Gson().toJson(contact1)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            if(added)
            {
                jsonArray.put(new JSONObject(new Gson().toJson(contact)));
            }
            jsonObject.put(CONTACTSKEY,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notification.setContactsList(jsonObject.toString());
        getModel().getNotificationDatabaseHelper().update(notification);
        //use local database to save the contact whitelist, and refresh med-library contact filter
        getModel().initializeNotifications();
        return true;
    }
}
