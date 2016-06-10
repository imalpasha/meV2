package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.ManageFlight.MF_ChangeContactFragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_ChangeContactFragment.class,
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
