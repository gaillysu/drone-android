package com.dayton.drone.ble.model.request.contacts;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class UpdateContactsFilterRequest extends RequestBase{
    public final static byte HEADER = (byte)0x1A;
    final String contact;
    final byte operationMode;
    final String contactID;

    public UpdateContactsFilterRequest(Context context, String contact, byte operationMode, String contactID) {
        super(context);
        this.contact = contact;
        this.operationMode = operationMode;
        this.contactID = contactID;
    }

    @Override
    public byte[] getRawData() {
        int dataLength = 4+contact.length();
        if(operationMode==3) {
            dataLength = 4+contact.length()+contactID.length();
        }

        byte[] rawData = new byte[dataLength];
        rawData[0] = (byte)0x80;
        rawData[1] = HEADER;
        rawData[2] = (byte)contact.length();
        System.arraycopy(contact.getBytes(),0,rawData,3,contact.length());
        rawData[3+contact.length()] = operationMode;
        if(operationMode==3){
            System.arraycopy(contactID.getBytes(),0,rawData,4+contact.length(),contactID.length());
        }

        return rawData;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
