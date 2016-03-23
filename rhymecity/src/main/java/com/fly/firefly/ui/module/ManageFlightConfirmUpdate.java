package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.ManageFlight.CommitChangeFragment;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
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
