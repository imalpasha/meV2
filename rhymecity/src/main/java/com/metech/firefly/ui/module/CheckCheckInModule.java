package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.SlidePage.NearKioskFragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = NearKioskFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class CheckCheckInModule {

    private final ManageFlightPrenter.ManageFlightView homeView;

    public CheckCheckInModule(ManageFlightPrenter.ManageFlightView homeView) {
        this.homeView = homeView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideHomePresenter(Bus bus) {
        return new ManageFlightPrenter(homeView, bus);
    }
}
