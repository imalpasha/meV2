package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.PersonalDetailFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
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
