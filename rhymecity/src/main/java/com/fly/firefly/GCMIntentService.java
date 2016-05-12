package com.fly.firefly;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fly.firefly.R;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.base.BaseFragmentActivity;
import com.fly.firefly.ui.activity.BeaconV2.BoardingGateActivity;
import com.fly.firefly.ui.activity.BeaconV2.PushNotificationV1;
import com.fly.firefly.ui.activity.GeoFencing.GenFencingActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.PushNotification.MainActivity;
import com.fly.firefly.ui.activity.PushNotification.ServerUtilities;
import com.fly.firefly.ui.activity.SplashScreen.PopupNotificationActivity;
import com.fly.firefly.ui.activity.SplashScreen.SplashScreenActivity;
import com.fly.firefly.ui.activity.SplashScreen.TokenActivity;
import com.fly.firefly.utils.RealmObjectController;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gms.common.api.BooleanResult;
import com.google.gson.Gson;

import static com.fly.firefly.ui.activity.PushNotification.CommonUtilities.SENDER_ID;
import static com.fly.firefly.ui.activity.PushNotification.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        Log.e("Registering", "tRUE");
        displayMessage(context, "Your device registred with GCM");
        //ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);

        //back to splash screen
        //BaseFragment.splashScreen(context,registrationId);
        TokenActivity.splash(context,registrationId);

    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, "Device Unregistered");
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        Log.e("intent", intent.getClass().getCanonicalName());
        String message = intent.getExtras().getString("message");
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
        //BaseFragmentActivity.setAppStatus(true);
       // Log.e("CheckAppStatus", Boolean.toString(BaseFragmentActivity.getAppStatus()));

    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = "Register";
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        Log.e("Registering", "NOT REGISTER");

        displayMessage(context, "Register");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.register,errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */

    public class GCMClass {

        private String title;
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    private void generateNotification(Context context, String message) {

        Gson gson = new Gson();
        GCMClass obj = gson.fromJson(message, GCMClass.class);

//        String[] parts = message.split("/");
//        String part1 = parts[0]; // 004
//        String part2 = parts[1]; // 034556\
//        Log.e("Part1 " + part1, "Part2 " + part2);

        //convert string to json
        Intent viewIntent;
        //if(LifeCycleActivity.isApplicationVisible()){
            viewIntent = new Intent(context, PopupNotificationActivity.class);
            Log.e("Visibile","true");
       // }


        viewIntent.setAction("android.intent.action.MAIN");
        viewIntent.addCategory("android.intent.category.LAUNCHER");
        viewIntent.putExtra("MESSAGE", message);

        //save message to realm object
        RealmObjectController.clearNotificationMessage(context);
        RealmObjectController.saveNotificationMessage(context,obj.getBody(),obj.getTitle());

        PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder
                .setContentText(obj.getBody())
                .setContentTitle(String.format(obj.getTitle()))
                .setSmallIcon(R.drawable.departure_icon)
                .setColor(Color.argb(0x55, 0x00, 0x00, 0xff))
                .setTicker(String.format(obj.getTitle()));
        //Intent notificationIntent = new Intent(context, MapsActivity.class);
        //notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //notificationIntent.setAction(Intent.ACTION_MAIN);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notificationBuilder.setContentIntent(viewPendingIntent);
        notificationManager.notify(1, notificationBuilder.build());

        /*if(part2.equals("reminder")){
            Log.e("PART2","REMINDER");
            Intent viewIntent = new Intent(context, PushNotificationV1.class);
            viewIntent.putExtra("MESSAGE", message);
            viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
            notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
            notificationBuilder
                    .setContentText(message)
                    .setContentTitle(String.format("Firefly"))
                    .setSmallIcon(R.drawable.departure_icon)
                    .setColor(Color.argb(0x55, 0x00, 0x00, 0xff))
                    .setTicker(String.format("%1$s Fence: %2$s", "tEST", "tEST"));
            //Intent notificationIntent = new Intent(context, MapsActivity.class);
            //notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            //notificationIntent.setAction(Intent.ACTION_MAIN);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            notificationBuilder.setContentIntent(viewPendingIntent);
            notificationManager.notify(1, notificationBuilder.build());

        }else{

            Intent viewIntent = new Intent(context, BoardingGateActivity.class);
            //viewIntent.putExtra("MESSAGE", message);
            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewIntent);
            //viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
            //Log.e("Beacon", "True");
            //test();
        }*/


       /* int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
       // notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      */

    }

    public void test(){
        BeaconController.startRangeDeparture(this);
    }

}
