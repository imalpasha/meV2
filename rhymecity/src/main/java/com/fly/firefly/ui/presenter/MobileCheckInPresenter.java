package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.MobileCheckInPassengerReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.ui.object.ManageFlightObjV2;
import com.fly.firefly.ui.object.MobileCheckInPassenger;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MobileCheckInPresenter {

    public interface MobileCheckInView {
        void onCheckindataReceive(MobileCheckinReceive obj);
        void onUserPnrList(ListBookingReceive event);

    }

    public interface MobileCheckInView2 {
        void onCheckInPassenger(MobileCheckInPassengerReceive obj);
    }

    public interface MobileCheckInView3 {
        void onConfirmCheckInPassenger(MobileConfirmCheckInPassengerReceive obj);
    }


    private MobileCheckInView view;
    private MobileCheckInView2 view2;
    private MobileCheckInView3 view3;

    private final Bus bus;

    public MobileCheckInPresenter(MobileCheckInView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public MobileCheckInPresenter(MobileCheckInView2 view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public MobileCheckInPresenter(MobileCheckInView3 view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }

    public void getUserPNR(String username,String password,String module) {
        bus.post(new ManageFlightObjV2(username,password,module));

    }

    public void checkinFlight(MobileCheckinObj flightObj) {
        bus.post(new MobileCheckinObj(flightObj));
    }

    public void checkInPassenger(MobileCheckInPassenger flightObj) {
        bus.post(new MobileCheckInPassenger(flightObj));
    }

    public void confirmCheckInPassenger(MobileConfirmCheckInPassenger flightObj) {
        bus.post(new MobileConfirmCheckInPassenger(flightObj));
    }


    @Subscribe
    public void onRequestSuccess(MobileCheckinReceive event) {
        /*Save Session And Redirect To Homepage*/
        view.onCheckindataReceive(event);
    }

    @Subscribe
    public void onCheckInPassenger(MobileCheckInPassengerReceive event) {
        /*Save Session And Redirect To Homepage*/
        view2.onCheckInPassenger(event);
    }


    @Subscribe
    public void onUserPnrList(ListBookingReceive event) {
        view.onUserPnrList(event);
    }

    @Subscribe
    public void onConfirmCheckInPassenger(MobileConfirmCheckInPassengerReceive event) {
        /*Save Session And Redirect To Homepage*/
        view3.onConfirmCheckInPassenger(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
