package com.metech.firefly.ui.activity.GlobalPopup;

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

import com.metech.firefly.R;
import com.metech.firefly.ui.activity.PushNotification.AlertDialogManager;
import com.metech.firefly.ui.activity.PushNotification.ConnectionDetector;
import com.metech.firefly.ui.activity.PushNotification.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.metech.firefly.ui.activity.PushNotification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.metech.firefly.ui.activity.PushNotification.CommonUtilities.EXTRA_MESSAGE;
import static com.metech.firefly.ui.activity.PushNotification.CommonUtilities.SENDER_ID;

public class PopupActivity extends Activity {

    private String msj;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            msj = bundle.getString("msj");
            title = bundle.getString("title");

        }

        setAlertDialog(this,msj,title);
    }

    public static void setAlertDialog(Activity act, String msg, String title) {

        if (act != null) {
                new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(title)
                        .setContentText(msg)
                        .show();

        }
    }

}
