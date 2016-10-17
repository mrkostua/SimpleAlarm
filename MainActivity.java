package com.example.simplealarm2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    private Button bOn, bOff;
    private TextView tvActions;
    private TimePicker timePicker;
    private  Calendar calendar;
    private PendingIntent pendingIntent;
    private Intent ARintent;
    private AlarmManager alarmManager;
    private static final String TAG = "MainActivity";
    private boolean OnOffRemember = false ;




    @Override
        protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //initializes widgets(buttons,text_views)
        bOn = (Button) findViewById(R.id.bOn);
        bOff = (Button) findViewById(R.id.bOff);
        tvActions = (TextView) findViewById(R.id.tvActions);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        //create an instance(PRZYKŁAD) of a calendar
         calendar = Calendar.getInstance();

        //create an Intent to the  Alarm_Receiver class
         ARintent = new Intent(this,Alarm_Receiver.class);

        //initialize alarmManager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    //OnClick Method for Button - bOn
        public void bOnClickListener(View v)
    {
        OnOffRemember = true;
        //refresh an instant of calendar to get the exact hour
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        //setting calendar instance with the hour and minute that we picked on the timerPicker
        calendar.set(Calendar.HOUR_OF_DAY,  timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE,  timePicker.getCurrentMinute());


        //get the int values of the picked hour and minute
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // convert the int value to String value
        String sHour =  String.valueOf(hour);
        String sMinute =  String.valueOf(minute);

        //convert minute from 2:5 to  2:05
        if(minute<10)
            sMinute="0" + String.valueOf( minute);

        //method that changes the text in the TextView tvActions
        tvActions_editor("Alarm ON " + sHour + ":" + sMinute);

        // Method show Toast  about How much time left until alarm start
            HowMuchTimeToStart(currentHour,currentMinute,hour,minute);

        // sending the condition of alarm if true - alarm on , if false - alarm off
        ARintent.putExtra("Alarm condition",true);

        //pendingIntent that delays the intent until the specified calendar time
        pendingIntent = pendingIntent.getBroadcast(MainActivity.this,0,ARintent, PendingIntent.FLAG_UPDATE_CURRENT);

        // If the hour picked in the TimePicker longer than hour of Current time or equal
        //and if a minute is longer or equal than the  current minute (because hour may be same , but the minute less )
                if (hour>currentHour)
            {
                Log.i(TAG, "h current: " + currentHour + " alarm hour: " + timePicker.getCurrentHour() + "  Today");
                Log.i(TAG, "min current: " + currentMinute + " alarm min: " + timePicker.getCurrentMinute() + "  Today");
                    //set the alarm manager
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        //All other cases  alarmManager.setInexactRepeating  AlarmManager.INTERVAL_DAY
        if (hour < currentHour)
        {
            Log.i(TAG, "h current: " + currentHour + " alarm hour: " + timePicker.getCurrentHour() + "Next Day");
            Log.i(TAG, "min current: " + currentMinute + " alarm min: " + timePicker.getCurrentMinute() + "  Next Day");


            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_WEEK,calendar.get(Calendar.DAY_OF_WEEK)+1);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

            if(hour == currentHour)
        {
            if (minute < currentMinute)
            {
                Log.i(TAG,"h current: " + currentHour + " alarm hour: " + timePicker.getCurrentHour()+" Next Day ");
                Log.i(TAG, "min current: " + currentMinute + " alarm min: " + timePicker.getCurrentMinute() + "  Next Day");

                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.DAY_OF_WEEK,calendar.get(Calendar.DAY_OF_WEEK)+1);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            else
            {
                Log.i(TAG, "h current: " + currentHour + " alarm hour: " + timePicker.getCurrentHour() + "  Today");
                Log.i(TAG, "min current: " + currentMinute + " alarm min: " + timePicker.getCurrentMinute() + "  Today");
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        }




    // Show Toast  about How much time left until alarm start
        private void HowMuchTimeToStart(int currentHour, int currentMinute, int pickedHour, int pickedMinute)
    {
        // day in minutes
         final int dayInMinutes = 1440;
        // Difference between current hour or minute and picked hour or minute
        int differenceCP, resultHours, resultMinutes;
        int timeToStartAlarm =0;

        //24 hour format time convert to minutes
        int picked, current;
        picked = pickedHour * 60 + pickedMinute;
        current = currentHour * 60 + currentMinute;

        Log.i(TAG," h current: " + currentHour + "  alarm hour: " + pickedHour );
        Log.i(TAG," min current: " + currentMinute + "  alarm min: " + pickedMinute);

        if (pickedHour>currentHour)
        {
            timeToStartAlarm = picked - current;
        }
        if (pickedHour < currentHour)
        {
            //minutes to start
            differenceCP = current - picked;
            timeToStartAlarm = dayInMinutes - differenceCP;

        }
        if(pickedHour == currentHour)
        {
            if (currentMinute > pickedMinute )
            {
                differenceCP = current - picked;
                timeToStartAlarm = dayInMinutes - differenceCP;
            }
            else
            {
                timeToStartAlarm = picked - current;
            }
        }
        // convert from minutes to 24 hour format
        resultHours = timeToStartAlarm / 60;
        resultMinutes = timeToStartAlarm - resultHours * 60;
        //Show toast about how much time left to start
        Toast timeToStart = Toast.makeText(MainActivity.this,"   Until boom    " + resultHours + " hours :" + resultMinutes + "  minutes", Toast.LENGTH_LONG);
        timeToStart.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
        timeToStart.show();
    }

    //OnClick Method for Button - bOff
        public void bOffClickListener(View v)
    {
        //cancel the alarm
        // Only if Alarm was set (button On pressed)
        if(OnOffRemember)
        {
            // sending the condition of alarm if true - alarm on , if false - alarm off
            ARintent.putExtra("Alarm condition",false);
            sendBroadcast(ARintent);
            //Cancel the alarm
            alarmManager.cancel(pendingIntent);
            tvActions_editor("Alarm OFF");

            OnOffRemember = false;
        }
        else
        {
            Log.i(TAG, "AlarmManager is not set");
            tvActions_editor("Alarm is not set");
        }
    }

    //method thar changes the text in the TextView tvActions
        private void tvActions_editor(String s)
    {
        tvActions.setText(s);
    }
}
