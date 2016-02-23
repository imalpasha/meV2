package com.fly.firefly.ui.presenter;

import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class UpdateProfilePresenter {

    public interface UpdateProfileView {
        void onSuccessUpdate(UpdateProfileReceive obj);
    }

    private final UpdateProfileView view;
    private final Bus bus;

    public UpdateProfilePresenter(UpdateProfileView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onUpdateProfile(UpdateProfileRequest data) {
        bus.post(new UpdateProfileRequest(data));
    }


   @Subscribe
    public void onSuccessUpdate(UpdateProfileReceive event) {

        //Log.e("Messages", event.getStatus());
        //*Save Session And Redirect To Homepage*//*
        view.onSuccessUpdate(event.getUserObj());
    }
}
