package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.BookingFlight.PersonalDetailFragment;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PersonalDetailFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class PersonalDetailModule {

    private final BookingPresenter.PassengerInfoView personalDetailView;

    public PersonalDetailModule(BookingPresenter.PassengerInfoView personalDetailView) {
        this.personalDetailView = personalDetailView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(personalDetailView, bus);
    }
}
