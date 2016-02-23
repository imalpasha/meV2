package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.ContactInfoFragment;
import com.fly.firefly.ui.activity.BookingFlight.PersonalDetailFragment;
import com.fly.firefly.ui.activity.ManageFlight.MF_EditPassengerFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
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
