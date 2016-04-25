package com.dayton.drone.ble.model.request.contactsfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/22.
 */
public class UpdateContactsApplicationRequest extends RequestBase{
    public final static byte HEADER = (byte)0x1B;

    final String appPackage;
    final byte operationMode;
    final byte searchFields;

    public UpdateContactsApplicationRequest(Context context, String appPackage, byte operationMode, byte searchFields) {
        super(context);
        this.appPackage = appPackage;
        this.operationMode = operationMode;
        this.searchFields = searchFields;
    }

    @Override
    public byte[] getRawData() {
        byte[] rawData = new byte[5+appPackage.length()];
        rawData[0] = (byte) 0x80;
        rawData[1] = HEADER;
        rawData[2] = (byte)appPackage.length();
        System.arraycopy(appPackage.getBytes(),0,rawData,3,appPackage.length());
        rawData[3+appPackage.length()] = operationMode;
        rawData[4+appPackage.length()] = searchFields;
        return rawData;
    }

    @Override
    public byte[][] getRawDataEx() {
        //NOTICE HERE MUST RETURN NULL, due to getRawData() got override,see above
        return null;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
