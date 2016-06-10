package com.metech.firefly.ui.presenter;

import com.metech.firefly.api.obj.AboutUsReceive;
import com.metech.firefly.ui.object.AboutUs;
import com.metech.firefly.utils.SharedPrefManager;
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
