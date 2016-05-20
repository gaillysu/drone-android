package com.dayton.drone.ble.model.request.notificationfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPackageConverter;

/**
 * Created by med on 16/4/22.
 */
public class UpdateNotificationFilterRequest extends RequestBase {
    public final static byte HEADER = (byte)0xB;
    final byte operationMode;
    final String appPackage;
    public UpdateNotificationFilterRequest(Context context, byte operationMode, String appPackage) {
        super(context);
        this.operationMode = operationMode;
        this.appPackage = appPackage;
    }

    @Override
    public byte[][] getRawDataEx() {
        byte[] rawData = new byte[3+appPackage.length()];
        rawData[0] = HEADER;
        rawData[1] = operationMode;
        rawData[2] = (byte)appPackage.length();
        System.arraycopy(appPackage.getBytes(),0,rawData,3,appPackage.length());
        return SplitPackageConverter.rawData2Packages(rawData, Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
