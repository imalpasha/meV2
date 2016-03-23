package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.ManageFlight.MF_SentItineraryFragment;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_SentItineraryFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightItinenary {

    private final ManageFlightPrenter.SendItinenary loginView;

    public ManageFlightItinenary(ManageFlightPrenter.SendItinenary loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
