package com.metech.firefly.ui.presenter;

import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.api.obj.DeviceInfoSuccess;
import com.metech.firefly.api.obj.SplashFailedConnect;
import com.metech.firefly.ui.object.DeviceInformation;
import com.metech.firefly.ui.object.PushNotificationObj;
import com.metech.firefly.utils.SharedPrefManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class HomePresenter {

    private SharedPrefManager pref;

    public interface PushNotification {

    }

    public interface HomeView {

    }

    public interface SplashScreen {
        void loadingSuccess(DeviceInfoSuccess obj);
        void onConnectionFailed();

    }

    private HomeView view;
    private SplashScreen view2;
    private PushNotification view3;


    private final Bus bus;

    public HomePresenter(HomeView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public HomePresenter(SplashScreen view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public HomePresenter(PushNotification view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }



    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void deviceInformation(DeviceInformation info) {
        bus.post(new DeviceInformation(info));
    }

    public void onRegisterNotification(PushNotificationObj info) {
        bus.post(new PushNotificationObj(info));
    }



    @Subscribe
    public void onSuccessSendDeviceInformation(DeviceInfoSuccess event) {
        pref = new SharedPrefManager(MainFragmentActivity.getContext());
        if (view2!=null){
            view2.loadingSuccess(event);
        }
    }

    @Subscribe
    public void onFailedConnect(SplashFailedConnect event) {
        view2.onConnectionFailed();
    }
}
