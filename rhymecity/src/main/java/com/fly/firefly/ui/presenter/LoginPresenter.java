package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.SplashFailedConnect;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.PasswordRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LoginPresenter {

    public interface LoginView {

        void onLoginSuccess(LoginReceive obj);
        void onLoginFailed(String dumm);
        void onRequestPasswordSuccess(ForgotPasswordReceive obj);
        //  void onPasswordRequestFailed(ForgotPasswordReceive obj);
        //void onPasswordRequesFailed(String dumm);


    }

    private final LoginView view;
    private final Bus bus;

    public LoginPresenter(LoginView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void loginFunction(LoginRequest data) {
        Log.e("xxxx",data.getUsername());
        bus.post(new LoginRequest(data));
    }


    public void forgotPassword(PasswordRequest data) {
        Log.e("xxxx",data.getEmail());
        bus.post(new PasswordRequest(data));
    }

    @Subscribe
    public void onUserSuccessLogin(LoginReceive event) {

        /*Save Session And Redirect To Homepage*/
        view.onLoginSuccess(event.getUserObj());
    }



    @Subscribe
    public void onUserSuccessLogin(SplashFailedConnect event) {

        /*Save Session And Redirect To Homepage*/
        view.onLoginFailed(event.getDummy());
    }


    @Subscribe
    public void onUserSuccessReqPassword(ForgotPasswordReceive obj) {

        //*Save Session And Redirect To Homepage*//*
        view.onRequestPasswordSuccess(obj.getUserObj());
    }

   // @Subscribe
    //public void onUserFailedReqPassword(SplashFailedConnect event) {
    /*@Subscribe
    public void onUserFailedReqPassword(FailedConnectToServer event) {
>>>>>>> 6401c7b9c51ce353bbc670f4f01fdaaabf4b7ad8
//
        /*//*Save Session And Redirect To Homepage*//**//*
      view.onPasswordRequestFailed(event.getDummy());
   }*/

}
