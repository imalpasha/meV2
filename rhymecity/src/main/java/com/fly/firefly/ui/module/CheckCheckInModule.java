package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.Homepage.HomeFragment;
import com.fly.firefly.ui.activity.SlidePage.NearKioskFragment;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
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
