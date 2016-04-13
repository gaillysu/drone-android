package com.dayton.drone.ble.model.request.base;

import android.content.Context;

import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;

import net.medcorp.library.ble.datasource.GattAttributesDataSource;
import net.medcorp.library.ble.model.request.BLERequestData;

/**
 * Created by med on 16/4/12.
 * for Drone project, the packet length is not fixed, and the number of packet also not fixed,
 * so you must override  getRawDataEx()
 * now getRawData() is no useful
 */
public abstract  class RequestBase extends BLERequestData {

    public RequestBase(Context context) {
        super(new GattAttributesDataSourceImpl(context));
    }

    @Override
    public byte[] getRawData() {
        return null;
    }

    @Override
    abstract public byte[][] getRawDataEx();
}
