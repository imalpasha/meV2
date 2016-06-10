package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.ManageFlight.MF_Fragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_Fragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightModule {

    private final ManageFlightPrenter.ManageFlightView loginView;

    public ManageFlightModule(ManageFlightPrenter.ManageFlightView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
