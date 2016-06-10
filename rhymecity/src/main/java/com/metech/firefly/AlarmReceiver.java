package com.metech.firefly;

/**
 * Created by Dell on 1/15/2016.
 */
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context);


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here update the widget/remote views.
       // Bundle extras = intent.getExtras();
       //  StringBuilder msgStr = new StringBuilder();

       // if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
       //     msgStr.append("One time Timer : ");
       // }
       // Format formatter = new SimpleDateFormat("hh:mm:ss a");
       // msgStr.append(formatter.format(new Date()));

       // Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        //Release the lock
        wl.release();

    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        Log.e("Alarm Cancel", "True");
    }

    private void createNotification(Context con) {

        CancelAlarm(con);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(con);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder
                .setContentText("test")
                .setContentTitle(String.format("Fence %1$s", "test"))
                .setSmallIcon(R.drawable.departure_icon)
                .setColor(Color.argb(0x55, 0x00, 0x00, 0xff))
                .setTicker(String.format("%1$s Fence: %2$s", "test", 1));
        //Intent notificationIntent = new Intent(context, MapsActivity.class);
        //notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //notificationIntent.setAction(Intent.ACTION_MAIN);
        NotificationManager notificationManager = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        //notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(R.id.notification, notificationBuilder.build());

       /* Intent dismissIntent = new Intent(context, HomeActivity.class);
        dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(context, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(context, HomeActivity.class);
        snoozeIntent.setAction(CommonConstants.ACTION_SNOOZE);
        PendingIntent piSnooze = PendingIntent.getService(context, 0, snoozeIntent, 0);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder
                .setContentText(fenceId)
                .setContentTitle(String.format("Fence %1$s", fenceState))
                .setSmallIcon(R.drawable.departure_icon)
                .setColor(Color.argb(0x55, 0x00, 0x00, 0xff))
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Notification"))
                .addAction(R.drawable.departure_icon, "On Bluetooth", piDismiss)
                .addAction(R.drawable.arrival_icon, "Cancel", piSnooze)
                .setTicker(String.format("%1$s Fence: %2$s", fenceState, fenceId));

        Intent notificationIntent = new Intent(context, GenFencingActivity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(R.id.notification, notificationBuilder.build());
        */

    }
}
