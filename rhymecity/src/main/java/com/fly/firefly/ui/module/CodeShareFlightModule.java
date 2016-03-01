package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.CodeShareFlightListFragment;
import com.fly.firefly.ui.activity.BookingFlight.FireflyFlightListFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = CodeShareFlightListFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class CodeShareFlightModule {

    private final BookingPresenter.ListFlightView flightDetailViewView;

    public CodeShareFlightModule(BookingPresenter.ListFlightView flightDetailViewView) {
        this.flightDetailViewView = flightDetailViewView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(flightDetailViewView, bus);
    }
}
