package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.FlightDetailFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = FlightDetailFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SelectFlightModule {

    private final BookingPresenter.ListFlightView flightDetailViewView;

    public SelectFlightModule(BookingPresenter.ListFlightView flightDetailViewView) {
        this.flightDetailViewView = flightDetailViewView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(flightDetailViewView, bus);
    }
}
