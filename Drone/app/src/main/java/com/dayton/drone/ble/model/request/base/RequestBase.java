package com.dayton.drone.ble.model.request.base;

import android.content.Context;

import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;

import net.medcorp.library.ble.model.request.BLERequestData;

/**
 * Created by med on 16/4/12.
 * for Drone project, the packet length is not fixed, and the number of packet also not fixed,
 * so you must override one of getRawDataEx() and getRawData()
 * if you both override them, only the getRawData() is enable, getRawDataEx() will get ignored
 */
public abstract  class RequestBase extends BLERequestData {

    protected RequestBase(Context context) {
        super(new GattAttributesDataSourceImpl(context));
    }

    @Override
    public byte[] getRawData() {
        return null;
    }

    @Override
    public byte[][] getRawDataEx(){
        return null;
    }
}
