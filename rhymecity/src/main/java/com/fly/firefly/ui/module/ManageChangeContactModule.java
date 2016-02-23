package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlightChangeContactFragment;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ManageFlightChangeContactFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageChangeContactModule {

    private final ManageFlightPrenter.ChangeContactView loginView;

    public ManageChangeContactModule(ManageFlightPrenter.ChangeContactView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
