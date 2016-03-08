package com.fly.firefly.ui.presenter;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.api.obj.AboutUsReceive;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.SplashFailedConnect;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.ui.object.AboutUs;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.object.PushNotificationObj;
import com.fly.firefly.ui.object.Signature;
import com.fly.firefly.utils.SharedPrefManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class AboutUsPresenter {

    private SharedPrefManager pref;

    public interface AboutUsView {
        void onRequestSuccess(AboutUsReceive event);
    }

    private AboutUsView view;

    private final Bus bus;

    public AboutUsPresenter(AboutUsView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void requestAboutUsInfo(AboutUs obj) {
        bus.post(new AboutUs(obj));
    }

    @Subscribe
    public void requestSuccess(AboutUsReceive event) {
        view.onRequestSuccess(event);
    }

}
