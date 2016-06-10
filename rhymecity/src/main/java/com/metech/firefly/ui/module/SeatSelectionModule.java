package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.BookingFlight.SeatSelectionFragment;
import com.metech.firefly.ui.presenter.BookingPresenter;
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
