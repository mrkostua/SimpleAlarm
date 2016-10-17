package com.example.simplealarm2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class RingtoneService extends Service
{
    private static final String TAG = "RingtoneService";

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        

        Boolean alarmCondition = intent.getExtras().getBoolean("Alarm condition");

        assert alarmCondition !=null;
       if(alarmCondition)
       {
           mediaPlayer = MediaPlayer.create(this, R.raw.ringtone1);
           mediaPlayer.start();
       }
       else if(!alarmCondition)
       {
           mediaPlayer.stop();
           mediaPlayer.reset();
       }

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}
