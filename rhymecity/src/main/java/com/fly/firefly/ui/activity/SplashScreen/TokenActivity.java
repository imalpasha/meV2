package com.fly.firefly.ui.activity.SplashScreen;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.PushNotification.AlertDialogManager;
import com.fly.firefly.ui.activity.PushNotification.ConnectionDetector;
import com.fly.firefly.ui.activity.PushNotification.WakeLocker;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import static com.fly.firefly.ui.activity.PushNotification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.fly.firefly.ui.activity.PushNotification.CommonUtilities.EXTRA_MESSAGE;
import static com.fly.firefly.ui.activity.PushNotification.CommonUtilities.SENDER_ID;

public class TokenActivity extends Activity {
    // label to display gcm messages
    TextView lblMessage;
    EditText textView;
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;

    public static String name;
    public static String email;
    String regId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {

            Intent home = new Intent(TokenActivity.this, SplashScreenActivity.class);
            home.putExtra("DEVICE_TOKEN", "");
            startActivity(home);
            finish();
            return;

        }

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        //GCMRegistrar.checkManifest(this);
        GCMRegistrar.checkManifest(this);

        lblMessage = (TextView) findViewById(R.id.lblMessage);
        textView = (EditText) findViewById(R.id.textView);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

        regId = GCMRegistrar.getRegistrationId(this);
        Log.e("regId", regId);

        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
            Log.e("regId", "1");

        } else {
            Log.e("regId", "2");
            // Device is already registered on GCM

            // Try to register again, but not in the UI thread.
            // It's also necessary to cancel the thread onDestroy(),
            // hence the use of AsyncTask instead of a raw thread.
            final Context context = this;
            mRegisterTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mRegisterTask = null;

                    Intent home = new Intent(TokenActivity.this, SplashScreenActivity.class);
                    home.putExtra("DEVICE_TOKEN", regId);
                    startActivity(home);
                    finish();

                    Log.e("REG ID background",regId);
                }

            };
            mRegisterTask.execute(null, null, null);
        }
        //}
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            lblMessage.append(newMessage + "\n");
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };


    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            //Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}
