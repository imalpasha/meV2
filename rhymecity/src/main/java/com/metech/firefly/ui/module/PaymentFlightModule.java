package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.BookingFlight.PaymentFlightFragment;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PaymentFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class PaymentFlightModule {

    private final BookingPresenter.PaymentFlightView paymentFlightView;

    public PaymentFlightModule(BookingPresenter.PaymentFlightView paymentFlightView) {
        this.paymentFlightView = paymentFlightView;
    }

    @Provides
    @Singleton
    BookingPresenter provideSearchFlightPresenter(Bus bus) {
        return new BookingPresenter(paymentFlightView, bus);
    }
}
