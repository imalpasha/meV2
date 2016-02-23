package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInFragment1;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MobileCheckInFragment1.class,
        addsTo = AppModule.class,
        complete = false
)
public class MobileCheckInModule1 {

    private final MobileCheckInPresenter.MobileCheckInView view;

    public MobileCheckInModule1(MobileCheckInPresenter.MobileCheckInView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    MobileCheckInPresenter provideMobileCheckInPresenter1(Bus bus) {
        return new MobileCheckInPresenter(view, bus);
    }
}
