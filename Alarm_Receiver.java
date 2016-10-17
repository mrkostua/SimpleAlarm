package com.example.simplealarm2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Alarm_Receiver extends BroadcastReceiver {
    private static final String TAG = "Alarm_Receiver";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "We are in the receiver.");

        Boolean alarmCondition = intent.getExtras().getBoolean("Alarm condition");
        //An intent to the ringtone service
        Intent serviceIntent = new Intent(context ,RingtoneService.class);
        serviceIntent.putExtra("Alarm condition", alarmCondition);
        //starts the serviceIntent
        context.startService(serviceIntent);



    }
}
