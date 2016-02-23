package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.SeatSelectionFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.ui.presenter.SeatSelectionPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SeatSelectionFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SeatSelectionModule {

    private final BookingPresenter.SeatSelectionView seatSelectionView;

    public SeatSelectionModule(BookingPresenter.SeatSelectionView seatSelectionView) {
        this.seatSelectionView = seatSelectionView;
    }

    @Provides
    @Singleton
    BookingPresenter provideSearchFlightPresenter(Bus bus) {
        return new BookingPresenter(seatSelectionView, bus);
    }
}
