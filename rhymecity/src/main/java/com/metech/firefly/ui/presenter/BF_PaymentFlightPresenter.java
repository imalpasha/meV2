package com.metech.firefly.ui.presenter;

import com.squareup.otto.Bus;

public class BF_PaymentFlightPresenter {

    public interface PaymentFlightView {

    }

    private final PaymentFlightView view;
    private final Bus bus;

    public BF_PaymentFlightPresenter(PaymentFlightView view, Bus bus) {
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
