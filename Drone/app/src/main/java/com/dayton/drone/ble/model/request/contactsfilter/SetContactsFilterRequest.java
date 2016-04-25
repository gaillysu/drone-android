package com.dayton.drone.ble.model.request.contactsfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class SetContactsFilterRequest extends RequestBase{
    public final static byte HEADER = (byte)0x18;
    final byte clearContactsList;
    final byte clearContactsApplication;

    public SetContactsFilterRequest(Context context, byte clearContactsList, byte clearContactsApplication) {
        super(context);
        this.clearContactsList = clearContactsList;
        this.clearContactsApplication = clearContactsApplication;
    }

    @Override
    public byte[] getRawData() {
        return new byte[]{(byte)0x80,HEADER,clearContactsList,clearContactsApplication};
    }

    @Override
    public byte[][] getRawDataEx() {
        return null;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
