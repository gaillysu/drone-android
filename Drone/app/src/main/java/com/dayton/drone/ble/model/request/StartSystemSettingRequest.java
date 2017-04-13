package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;

/**
 * Created by med on 16/4/13.
 */
public class StartSystemSettingRequest extends RequestBase{
    public final static byte HEADER = (byte)0x34;
    final int id;
    final  int operation;

    public StartSystemSettingRequest(Context context, int id, int operation) {
        super(context);
        this.id = id;
        this.operation = operation;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, (byte) id,(byte)operation,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }

    public enum StartSystemSettingID {
        AnalogMovement(1),Compass(2);
        final int id;
        StartSystemSettingID(int id) {
            this.id = id;
        }
        public  int rawValue() {return id;}
    }

    public enum CompassSettingOperationID {
        Stop(0),Start(1);
        final int id;
        CompassSettingOperationID(int id) {
            this.id = id;
        }
        public  int rawValue() {return id;}
    }

    public enum AnalogMovementSettingOperationID {
        Exit(0),
        Start(1),
        StopAllHands(0x10),
        SecondHandAdvanceOneStep(0x11),
        SecondHandReverseOneStep(0x12),
        SecondHandAdvanceMoreSteps(0x13),
        SecondHandReverseMoreSteps(0x14),
        MinuteHandAdvanceOneStep(0x15),
        MinuteHandReverseOneStep(0x16),
        MinuteHandAdvanceMoreSteps(0x17),
        MinuteHandReverseMoreSteps(0x18),
        HourHandAdvanceOneStep(0x19),
        HourHandReverseOneStep(0x1A),
        HourHandAdvanceMoreSteps(0x1B),
        HourHandReverseMoreSteps(0x1C);
        final int id;
        AnalogMovementSettingOperationID(int id) {
            this.id = id;
        }
        public  int rawValue() {return id;}
    }
}
