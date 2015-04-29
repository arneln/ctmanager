package com.cloudstaff.cstm.utils;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class Service extends android.app.Service {
    Alarm alarm = new Alarm();
    SharedPreference mPreference = new SharedPreference(Service.this);

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.SetAlarm(Service.this, mPreference.getUpdateMinutes());
        return START_STICKY;
    }


    public void onStart(Context context, Intent intent, int startId) {
        alarm.SetAlarm(context, mPreference.getUpdateMinutes());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}