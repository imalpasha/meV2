package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.ManageFlight.MF_SeatSelectionFragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
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
