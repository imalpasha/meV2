package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.ManageFlight.CommitChangeFragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = CommitChangeFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightConfirmUpdate {

    private final ManageFlightPrenter.ConfirmUpdateView loginView;

    public ManageFlightConfirmUpdate(ManageFlightPrenter.ConfirmUpdateView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
