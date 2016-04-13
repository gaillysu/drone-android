package com.dayton.drone.ble.model.request.base;

import android.content.Context;

import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;

import net.medcorp.library.ble.datasource.GattAttributesDataSource;
import net.medcorp.library.ble.model.request.BLERequestData;

/**
 * Created by med on 16/4/12.
 * for Drone project, getRawDataEx() must return null, Drone package is only one raw.
 */
public abstract  class RequestBase extends BLERequestData {

    public RequestBase(Context context) {
        super(new GattAttributesDataSourceImpl(context));
    }

    @Override
    public byte[][] getRawDataEx() {
        return null;
    }
}
