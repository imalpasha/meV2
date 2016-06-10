package com.metech.firefly.ui.presenter;

import com.squareup.otto.Bus;

public class BF_FlightDetailPresenter {

    public interface FlightDetailView {

    }

    private final FlightDetailView view;
    private final Bus bus;

    public BF_FlightDetailPresenter(FlightDetailView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
