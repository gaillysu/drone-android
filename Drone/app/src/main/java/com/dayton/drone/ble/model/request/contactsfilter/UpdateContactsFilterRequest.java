package com.dayton.drone.ble.model.request.contactsfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;

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
    public byte[][] getRawDataEx() {
        int dataLength = 3+contact.length();
        if(operationMode==3) {
            dataLength = 3+contact.length()+contactID.length();
        }

        byte[] rawData = new byte[dataLength];
        rawData[0] = HEADER;
        rawData[1] = (byte)contact.length();
        System.arraycopy(contact.getBytes(),0,rawData,2,contact.length());
        rawData[2+contact.length()] = operationMode;
        if(operationMode==3){
            System.arraycopy(contactID.getBytes(),0,rawData,3+contact.length(),contactID.length());
        }
        return SplitPacketConverter.rawData2Packets(rawData,Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
