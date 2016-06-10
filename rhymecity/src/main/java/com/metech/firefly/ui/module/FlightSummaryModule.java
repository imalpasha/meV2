package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.BookingFlight.FlightSummaryFragment;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = FlightSummaryFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class FlightSummaryModule {

    private final BookingPresenter.FlightSummaryView loginView;

    public FlightSummaryModule(BookingPresenter.FlightSummaryView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    BookingPresenter provideLoginPresenter(Bus bus) {
        return new BookingPresenter(loginView, bus);
    }
}
