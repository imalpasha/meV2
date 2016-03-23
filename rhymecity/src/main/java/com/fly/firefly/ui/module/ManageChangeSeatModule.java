package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.ManageFlight.MF_SeatSelectionFragment;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_SeatSelectionFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageChangeSeatModule {

    private final ManageFlightPrenter.ChangeSeatView loginView;

    public ManageChangeSeatModule(ManageFlightPrenter.ChangeSeatView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
