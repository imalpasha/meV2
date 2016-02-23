package com.fly.firefly.ui.activity.GeoFencing;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fly.firefly.BeaconController;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.BeaconV2.ArriveEntranceActivity;
import com.fly.firefly.ui.activity.BeaconV2.PushNotificationV1;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.SlidePage.NearKioskActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionReceiver extends WakefulBroadcastReceiver {
    
    public static final String TAG = GeofenceTransitionReceiver.class.getSimpleName();
    
    private Context context;
    private static NotificationManager notificationManager;

    public GeofenceTransitionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive(context, intent)");
        this.context = context;
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event != null){
            if(event.hasError()){
                onError(event.getErrorCode());
            } else {
                int transition = event.getGeofenceTransition();
                if(transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL || transition == Geofence.GEOFENCE_TRANSITION_EXIT){
                    String[] geofenceIds = new String[event.getTriggeringGeofences().size()];
                    for (int index = 0; index < event.getTriggeringGeofences().size(); index++) {
                        geofenceIds[index] = event.getTriggeringGeofences().get(index).getRequestId();
                    }
                    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                        onEnteredGeofences(geofenceIds);
                    } else {
                        onExitedGeofences(geofenceIds);
                    }
                }
            }
        }
    }

    protected void onEnteredGeofences(String[] geofenceIds) {
        for (String fenceId : geofenceIds) {
            Toast.makeText(context, String.format("Entering Me-tech Solution Fence Region", fenceId), Toast.LENGTH_SHORT).show();
            Log.i(TAG, String.format("Entered this fence: %1$s", fenceId));
            //startNotification();
            //createNotification(fenceId, "Entered");
            //triggerNotification(context,"Enter Region");
            //startNotification();
         //   anotherNotification();
            noti();
        }
    }

    protected void onExitedGeofences(String[] geofenceIds){
        for (String fenceId : geofenceIds) {
            Toast.makeText(context, String.format("Exited this fence: %1$s", fenceId), Toast.LENGTH_SHORT).show();
            Log.i(TAG, String.format("Exited this fence: %1$s", fenceId));
            createNotification(fenceId, "Exited");
            //triggerNotification(context,"Exit Region");

        }
    }

    protected void onError(int errorCode){
        Toast.makeText(context, String.format("onError(%1$d)", errorCode), Toast.LENGTH_SHORT).show();
        Log.e(TAG, String.format("onError(%1$d)", errorCode));
    }

    /**
     * Create our notification.
     *
     * @param fenceId the name of the Geofence
     * @param fenceState Entered, Exited or Dwell
     */


    private void createNotification(String fenceId, String fenceState) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder
                .setContentText(fenceId)
                .setContentTitle(String.format("Fence %1$s", fenceState))
                .setSmallIcon(R.drawable.departure_icon)
                .setColor(Color.argb(0x55, 0x00, 0x00, 0xff))
                .setTicker(String.format("%1$s Fence: %2$s", fenceState, fenceId));
        //Intent notificationIntent = new Intent(context, MapsActivity.class);
        //notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //notificationIntent.setAction(Intent.ACTION_MAIN);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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


    public void anotherNotification(){
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(false);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Test");

        builder.setSmallIcon(R.drawable.ic_launcher)
        builder.setTicker("hello")
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setSmallIcon(R.drawable.departure_icon)
        builder.setContentTitle("Geo-fence Activity")
        builder.setContentText("Ask user to on device bluetooth")
        builder.setDefaults(Notification.DEFAULT_ALL); // requires VIBRATE permission
        builder.setStyle(new NotificationCompat.BigTextStyle()
        builder.bigText("Notification"))
        builder.addAction(R.drawable.departure_icon, "On Bluetooth", piDismiss)
        builder.addAction(R.drawable.arrival_icon, "Cancel", piSnooze);*/

        /*
         * Sets the big view "big text" style and supplies the
         * text (the user's reminder message) that will be displayed
         * in the detail area of the expanded notification.
         * These calls are ignored by the support library for
         * pre-4.1 devices.
         */

        //Intent intent = new Intent(context, SimpleMusicPlayer.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(pendingIntent);

        Intent actionIntent = new Intent(context, HomeActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getService(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.addAction(android.R.drawable.ic_media_pause, "PAUSE", actionPendingIntent);

// if(artwork != null) {
//     builder.setLargeIcon(artwork);
// }
// builder.setContentText(artist);
// builder.setSubText(album);

// startForeground(R.id.notification_id, builder.build());
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager manager = (NotificationManager)context.getSystemService(ns);
        //manager.notify(123, builder.build());
    }


    private void noti(){
        Boolean bluetooth;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.isEnabled()){
            bluetooth = true;
            BeaconController.startRangeDeparture(context);
        }else{
            bluetooth = false;
        }

        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) context.getSystemService(ns);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context);
        nb.setTicker("Welcome!!");
        nb.setSmallIcon(R.drawable.arrival_icon);
       // if(bluetooth){

            Intent viewIntent;
            PendingIntent viewPendingIntent = null;

            viewIntent = new Intent(context, ArriveEntranceActivity.class);
            viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
            //viewIntent.putExtra("MESSAGE", "arrive at airport");
            nb.setContentText("** Welcome to Subang Airport **");
            nb.setContentIntent(viewPendingIntent);
            BeaconController.startRangeDeparture(context);

       // }else{
       //     nb.setContentText("** Turn bluetooth on for smart assist **");
       //     nb.addAction(android.R.drawable.ic_btn_speak_now, "Turn On", PendingIntent.getActivity(context, 0, new Intent(context, switchButtonListener.class), 0));
      //  }

        nb.setContentTitle("Firefly");
        nb.setPriority(Notification.PRIORITY_MAX);
        //nb.setContentIntent(getClickIntent());
        nb.setAutoCancel(true);
        nb.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        //nb.setLights(LED, LEDON, LEDOFF);
        //nb.addAction(android.R.drawable.ic_dialog_map, "Mapa", PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), 0));

        //String ns = Context.NOTIFICATION_SERVICE;
        //NotificationManager manager = (NotificationManager)context.getSystemService(ns);
        notificationManager.notify(4700, nb.build());

    }


   public static class switchButtonListener extends Activity {

       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);

           BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
           mBluetoothAdapter.enable();
           notificationManager.cancel(4700);
           //blueToothConnection();
           /*Start Scan on airport entrance*/
           BeaconController.startRangeDeparture(this);

           /*Start Geo-Fence Activity*/
           //gpsConnection(this);

           Intent loginPage = new Intent(this, ArriveEntranceActivity.class);
           loginPage.putExtra("MESSAGE", "arrive at airport");
           this.startActivity(loginPage);

           finish();
       }
   }




   /* public static class switchButtonListener extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Here", "I am here");
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.enable();
            notificationManager.cancel(1);
        }
    } */


    public static  void triggerNotification(Context context,String message)
    {
        int notificationId = 001;
        // Build intent for notification content
        //Intent viewIntent = new Intent(context, LoginActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        //PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                        .setContentTitle("GEOFENCE")
                        .setContentText(message);
        //.setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public final class CommonConstants {
        public CommonConstants() {
            // don't allow the class to be instantiated
        }
        // Milliseconds in the snooze duration, which translates
        // to 20 seconds.
        public static final int SNOOZE_DURATION = 20000;
        public static final int DEFAULT_TIMER_DURATION = 10000;
        public static final String ACTION_SNOOZE = "com.example.android.pingme.ACTION_SNOOZE";
        public static final String ACTION_DISMISS = "com.example.android.pingme.ACTION_DISMISS";
        public static final String ACTION_PING = "com.example.android.pingme.ACTION_PING";
        public static final String EXTRA_MESSAGE= "com.example.android.pingme.EXTRA_MESSAGE";
        public static final String EXTRA_TIMER = "com.example.android.pingme.EXTRA_TIMER";
        public static final int NOTIFICATION_ID = 001;
        public static final String DEBUG_TAG = "PingMe";
    }

}
