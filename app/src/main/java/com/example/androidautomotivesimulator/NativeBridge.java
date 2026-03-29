package com.example.androidautomotivesimulator;

public class NativeBridge {
    static {
        System.loadLibrary("sensors");
    }

    public native int getSpeedDelta();
    public native int getBatteryDelta();
}
