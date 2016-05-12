package com.fly.firefly.ui.activity.SplashScreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fly.firefly.Controller;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.PushNotification.ConnectionDetector;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PopupNotificationActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainFragmentActivity.setAppStatus("ready_for_notification");
        Log.e("MainFragmentActivity","true");


        if(Controller.getHomeStatus()) {
            this.finish();
        }else{
            Intent home = new Intent(this, TokenActivity.class);
            this.startActivity(home);
            this.finish();
            Log.e("Visibile","false");
        }
    }
}
