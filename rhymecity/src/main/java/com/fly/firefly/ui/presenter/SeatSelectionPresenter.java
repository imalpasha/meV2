package com.fly.firefly.ui.presenter;

import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.squareup.otto.Bus;

public class SeatSelectionPresenter {

    public interface SeatSelectionView {

    }

    private final SeatSelectionView view;
    private final Bus bus;

    public SeatSelectionPresenter(SeatSelectionView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onRhymesForWordRequested(String word) {
        bus.post(new RhymesRequestedEvent(word));
    }
}
