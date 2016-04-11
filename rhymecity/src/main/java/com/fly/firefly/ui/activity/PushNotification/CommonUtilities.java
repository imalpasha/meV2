package com.fly.firefly.ui.activity.PushNotification;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    //static final String SERVER_URL = "http://192.168.56.1:80/gcm_server_php/register.php";
    //static final String SERVER_URL = "http://izzatiabdullah.tk/register.php";
    static final String SERVER_URL = "http://52.23.200.129/izzati/gcmwebapp/insertuser.php";


    // Google project id

   //static final String SENDER_ID = "350778414071";
    //static final String SENDER_ID = "12013843051";
    public static final String SENDER_ID = "409451026725";
//AIzaSyB-7qW3LUNG9KIO5JsIxb8y-Mx7x2LbJ5Y
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "ZATY GCM";

   public static final String DISPLAY_MESSAGE_ACTION ="com.fly.firefly.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
