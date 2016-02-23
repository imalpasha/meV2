package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.PaymentFlightFragment;
import com.fly.firefly.ui.presenter.BF_PaymentFlightPresenter;
import com.fly.firefly.ui.presenter.BookingPresenter;
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
