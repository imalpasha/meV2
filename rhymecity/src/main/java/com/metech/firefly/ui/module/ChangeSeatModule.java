package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.ManageFlight.MF_EditPassengerFragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_EditPassengerFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class ChangeSeatModule {

    private final ManageFlightPrenter.ChangePassengerInfoView contactInfoView;

    public ChangeSeatModule(ManageFlightPrenter.ChangePassengerInfoView contactInfoView) {
        this.contactInfoView = contactInfoView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideFlightDetailPresenter(Bus bus) {
        return new ManageFlightPrenter(contactInfoView, bus);
    }
}
