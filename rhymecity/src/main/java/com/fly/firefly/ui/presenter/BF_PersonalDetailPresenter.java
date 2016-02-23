package com.fly.firefly.ui.presenter;

import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.squareup.otto.Bus;

public class BF_PersonalDetailPresenter {

    public interface PersonalDetailView {

    }

    private final PersonalDetailView view;
    private final Bus bus;

    public BF_PersonalDetailPresenter(PersonalDetailView view, Bus bus) {
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
