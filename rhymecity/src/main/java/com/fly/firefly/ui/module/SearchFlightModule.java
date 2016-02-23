package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.SearchFlightFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SearchFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SearchFlightModule {

    private final BookingPresenter.SearchFlightView searchFlightView;

    public SearchFlightModule(BookingPresenter.SearchFlightView searchFlightView) {
        this.searchFlightView = searchFlightView;
    }

    @Provides
    @Singleton
    BookingPresenter provideSearchFlightPresenter(Bus bus) {
        return new BookingPresenter(searchFlightView, bus);
    }
}
