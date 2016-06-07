package com.dayton.drone.ble.server;

import java.util.UUID;

/**
 * Created by med on 16/6/6.
 */
public class GattServerAttributes {
    public static UUID NOTIFICATION_SERVICE = UUID.fromString("F0BA-3124-6CAC-4C99-9089-4B0A-1DF4-5002");
    public static UUID NOTIFICATION_ALERT_CHARACTERISTICS = UUID.fromString("F0BA-3125-6CAC-4C99-9089-4B0A-1DF4-5002");
    public static UUID NOTIFICATION_CONTROL_POINT_CHARACTERISTICS = UUID.fromString("F0BA-3126-6CAC-4C99-9089-4B0A-1DF4-5002");
    public static UUID NOTIFICATION_DATA_CHARACTERISTICS = UUID.fromString("F0BA-3127-6CAC-4C99-9089-4B0A-1DF4-5002");

    public static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}
