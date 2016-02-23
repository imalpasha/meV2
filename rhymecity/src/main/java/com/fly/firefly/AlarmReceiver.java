package com.fly.firefly;

/**
 * Created by Dell on 1/15/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

@Override
        public void onReceive(Context context, Intent intent) {
            try {

                BeaconController.startRangeDeparture(context);

            } catch (Exception e) {
                Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }

}
