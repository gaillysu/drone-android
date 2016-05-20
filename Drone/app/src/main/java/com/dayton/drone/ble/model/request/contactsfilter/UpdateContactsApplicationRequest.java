package com.dayton.drone.ble.model.request.contactsfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPackageConverter;

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
    public byte[][] getRawDataEx() {
        byte[] rawData = new byte[4+appPackage.length()];
        rawData[0] = HEADER;
        rawData[1] = (byte)appPackage.length();
        System.arraycopy(appPackage.getBytes(),0,rawData,2,appPackage.length());
        rawData[2+appPackage.length()] = operationMode;
        rawData[3+appPackage.length()] = searchFields;
        return SplitPackageConverter.rawData2Packages(rawData, Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
