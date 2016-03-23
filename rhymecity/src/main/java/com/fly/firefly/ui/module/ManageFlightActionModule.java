package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.ManageFlight.MF_ActionFragment;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_ActionFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightActionModule {

    private final ManageFlightPrenter.ManageFlightView loginView;

    public ManageFlightActionModule(ManageFlightPrenter.ManageFlightView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
