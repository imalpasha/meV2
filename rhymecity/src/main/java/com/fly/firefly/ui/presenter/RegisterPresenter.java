package com.fly.firefly.ui.presenter;

import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.ui.object.RegisterObj;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class RegisterPresenter {

    public interface RegisterView {
        void onSuccessRegister(RegisterReceive obj);
    }

    private final RegisterView view;
    private final Bus bus;

    public RegisterPresenter(RegisterView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onRequestRegister(RegisterObj obj) {
        bus.post(new RegisterObj(obj));
    }


    @Subscribe
    public void onUserSuccessRegister(RegisterReceive event) {

        //Log.e("Messages", event.getStatus());
        /*Save Session And Redirect To Homepage*/
        view.onSuccessRegister(event.getUserObj());
    }
}
