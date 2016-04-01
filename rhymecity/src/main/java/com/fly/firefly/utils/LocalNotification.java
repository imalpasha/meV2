package com.fly.firefly.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fly.firefly.AlarmReceiver;
import com.fly.firefly.base.BaseFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Dell on 3/17/2016.
 */
public class LocalNotification extends BaseFragment {

    public static void convert(Activity act){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // This step is crucial
        TimeZone malaysianTimeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur");
        dateFormat.setTimeZone(malaysianTimeZone);

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        //get current date time with Date()
        Date xx = new Date();

        String someDate = "2016-03-17 14:25:00";
        //String someDate = "2016/03/17 10:51:00";

        Date date = null;
        try {
            date = dateFormat.parse(someDate);
        }catch (Exception e){

        }
        System.out.println(dateFormat.format(date));
        System.out.println(dateFormat.format(xx));

        Log.e("M1", Long.toString(date.getTime()));
        Log.e("M2", Long.toString(xx.getTime()));

        Log.e("Total", Long.toString(date.getTime() - xx.getTime()));
        Long totalMilisecond = Math.abs(date.getTime() - xx.getTime());

        trySetAlarm(act,totalMilisecond);
        Log.e("TIme to run",Long.toString(totalMilisecond));
    }

    public static Boolean dateCompare(String date){

        boolean status = false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Log.e("c",cal.getTime().toString());

        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        }catch (Exception e){
            Log.e("message",e.getMessage());
        }

        //if(date1.before(cal.getTime())){
         //   status = true;
        //}

        return status;
    }

    public static Boolean compare(String x1,String x2){

        boolean status = false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(x1);
            date2 = sdf.parse(x2);

        }catch (Exception e){
        }

        if(date2.before(date1)){
            status = true;
        }

        return status;
    }


    public static void trySetAlarm(Activity act,Long time){

        AlarmManager am=(AlarmManager)act.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(act, AlarmReceiver.class);
        intent.putExtra("ONE_TIME", Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(act, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time), time , pi);
    }

}
