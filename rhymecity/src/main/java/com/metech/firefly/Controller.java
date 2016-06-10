package com.metech.firefly;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.BookingFlight.SearchFlightActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.activity.PasswordExpired.ChangePasswordActivity;
import com.metech.firefly.ui.activity.SplashScreen.SplashScreenActivity;
import com.metech.firefly.ui.activity.Terms.Terms;
import com.metech.firefly.utils.SharedPrefManager;
import com.metech.firefly.utils.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Dell on 1/5/2016.
 */
public class Controller extends BaseFragment {

    private static boolean homeStatus;

    public static boolean getHomeStatus(){
        return homeStatus;
    }

    public static void setHomeStatus(){
        homeStatus = true;
    }

    public static void clearAll(Activity act){
        SharedPrefManager  pref = new SharedPrefManager(act);
        pref.clearSignatureFromLocalStorage();
        pref.clearPassword();
        pref.clearUserEmail();
        pref.setLoginStatus("N");
        Log.e("SUCCESS","ok");
    }


    private static SweetAlertDialog pDialog;

    public static void clickableBanner(Activity act,String page){

        Intent bannerIntent;
        if(page.equals("booking")){
            bannerIntent = new Intent(act,SearchFlightActivity.class);
            act.startActivity(bannerIntent);
        }else if(page.equals("faq")){
            bannerIntent = new Intent(act,Terms.class);
            act.startActivity(bannerIntent);
        }
    }

    public static boolean connectionAvailable(Activity act){

        Boolean internet;
        internet = Utils.isNetworkAvailable(act);

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
            setAlertDialog(act, message,"Validation Error");

        } else if (objStatus.equals("401")) {
            status = false;
            setSignatureInvalid(act,message);
            pref.clearSignatureFromLocalStorage();

        }else if(objStatus.equals("force_logout")){
            Controller.clearAll(act);
            resetPage(act);
        }else if(objStatus.equals("503")){
            //Controller.clearAll(act);
            //resetPage(act);
            setAlertMaintance(act,message,SplashScreenActivity.class,"Sorry!");

        }
        else if (objStatus.equals("change_password")) {
            goChangePasswordPage(act);
        }else if(objStatus.equals("error")) {
            //croutonAlert(getActivity(),obj.getMessage());
            //setSuccessDialog(getActivity(), obj.getMessage(), getActivity(), SearchFlightActivity.class);
            setAlertDialog(act,message,"Validation Error");
        }
        return status;

    }

    public void retry(){

    }

    //Redirect
    public static void goChangePasswordPage(Activity act){
        Intent loginPage = new Intent(act, ChangePasswordActivity.class);
        act.startActivity(loginPage);
        act.finish();
    }

    //Redirect
    public static void resetPage(Activity act){
        Intent loginPage = new Intent(act, HomeActivity.class);
        act.startActivity(loginPage);
        act.finish();
    }

}
