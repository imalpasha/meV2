package com.fly.firefly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.SearchFlightActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.PasswordExpired.ChangePasswordActivity;
import com.fly.firefly.ui.activity.SplashScreen.SplashScreenFragment;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Dell on 1/5/2016.
 */
public class Controller extends BaseFragment {

    private static SweetAlertDialog pDialog;

    public static boolean connectionAvailable(Activity act){

        Boolean internet;
        if (!Utils.isNetworkAvailable(act)) {
            internet = false;
        }else{
            internet = true;
        }

        return internet;
    }

    public static boolean getRequestStatus(String objStatus,String message,Activity act) {

        SharedPrefManager pref;
        pref = new SharedPrefManager(act);

        Boolean status = false;
        if (objStatus.equals("success") || objStatus.equals("Redirect")) {
            status = true;

        } else if (objStatus.equals("error") || objStatus.equals("error_validation")) {
            status = false;
            setAlertDialog(act, message);

        } else if (objStatus.equals("401")) {
            status = false;
            setSignatureInvalid(act,message);
            pref.clearSignatureFromLocalStorage();

        }
        else if (objStatus.equals("change_password")) {
            pref.setLoginStatus("Y");
            goChangePasswordPage(act);
        }else if(objStatus.equals("error")) {
            //croutonAlert(getActivity(),obj.getMessage());
            //setSuccessDialog(getActivity(), obj.getMessage(), getActivity(), SearchFlightActivity.class);
            setAlertDialog(act,message);
        }
        return status;

    }

    //Redirect
    public static void goChangePasswordPage(Activity act){
        Intent loginPage = new Intent(act, ChangePasswordActivity.class);
        act.startActivity(loginPage);
        act.finish();
    }



}
