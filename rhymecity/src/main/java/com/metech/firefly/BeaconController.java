package com.metech.firefly;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.estimote.sdk.Utils;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.BeaconV2.BoardingGateActivityV2;
import com.metech.firefly.ui.activity.SlidePage.NearKioskActivity;
import com.metech.firefly.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dell on 1/15/2016.
 */
public class BeaconController extends BaseFragment {

    private static Activity instance;
    private static  BeaconManager beaconManager;
    private static Region region;
    private static Boolean departure_gate = true;
    private static Boolean arrive_airport = true;
    private SharedPrefManager pref;
    private static String msg;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        pref = new SharedPrefManager(getActivity());

        /*CheckUserStatus*/
        HashMap<String, String> initLoginStatus = pref.getLoginStatus();
        String isLogin = initLoginStatus.get(SharedPrefManager.ISLOGIN);

        if(isLogin != null){
            HashMap<String, String> initUsername = pref.getUsername();
            msg = initUsername.get(SharedPrefManager.USERNAME);
        }
    }

    public static void startRangeDeparture(final Context act){

        beaconManager = new BeaconManager(act);
        region = new Region("region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });


        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    double nearestBeaconDistance = Utils.computeAccuracy(nearestBeacon);

                    if (nearestBeacon.getMinor() == 2117 && nearestBeaconDistance < 1) {

                        if (arrive_airport) {
                            triggerNotification2(act,"ARRIVE_AIRPORT");
                            arrive_airport = false;
                            beaconManager.stopRanging(region);

                        }

                    }else if (nearestBeacon.getMinor() == 40462 && nearestBeaconDistance < 1) {

                        if (departure_gate) {
                            triggerNotification2(act,"KIOSK");
                            departure_gate = false;
                            beaconManager.stopRanging(region);

                        }

                    }

                }
            }
        });

    }

    public static void startRangeBoardingGate(final Context act){
        Log.e("startRangeBoardingGate","startRangeBoardingGate");

        beaconManager = new BeaconManager(act);
        region = new Region("region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 17407, 28559);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    double nearestBeaconDistance = Utils.computeAccuracy(nearestBeacon);
                    Log.e("nearestBeaconDistance",Double.toString(nearestBeaconDistance));
                    if (nearestBeacon.getMinor() == 28559 && nearestBeaconDistance < 1) {

                        triggerNotification(act, "Your flight will depart in 30 minutes. Have a pleasant journey");
                        //setAlertDialogV2(act, "Have a safe journey dude");
                        beaconManager.stopRanging(region);

                    }else if(nearestBeacon.getMinor() == 28559 && nearestBeaconDistance > 1){
                        //setAlertDialogV2(act, "Please come to Boarding Gate 14");
                        triggerNotification(act, "Your flight will depart in 30 minutes. Please go to Departure Gate immediately");
                        beaconManager.stopRanging(region);
                    }

                }
            }
        });


    }

    public static void triggerNotification(Context context,String message)
    {

        /*LayoutInflater li = LayoutInflater.from(context);
        final View myView = li.inflate(R.layout.boarding_gate_message, null);
        TextView cont = (TextView)myView.findViewById(R.id.txtMessage);
        cont.setText(message);
        myView.invalidate();*/

        //Log.e(message, message);
        //setAlertDialogV2(context,message);


        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(context, BoardingGateActivityV2.class);
        viewIntent.putExtra("MESSAGE", message);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.departure_icon)
                        .setContentTitle("Firefly")
                        .setContentText("Flight Departure Reminder")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());



    }

    public static void triggerNotification2(Context context,String act)
    {

        Intent viewIntent;
        PendingIntent viewPendingIntent = null;
        String msg =  "";
        String title =  "";

        viewIntent = new Intent(context, NearKioskActivity.class);
        viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);

        title = "Entrance";
        msg = "Proceed to check-in";

        int notificationId = 001;

        // Build intent for notification content
        //Intent viewIntent = new Intent(this, this);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        //PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);



        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.departure_icon)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setContentIntent(viewPendingIntent);
        notificationBuilder.setAutoCancel(true);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());


    }
}