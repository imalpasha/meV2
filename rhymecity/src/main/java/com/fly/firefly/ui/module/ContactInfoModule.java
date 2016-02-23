package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.ContactInfoFragment;
import com.fly.firefly.ui.activity.BookingFlight.PersonalDetailFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ContactInfoFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class ContactInfoModule {

    private final BookingPresenter.ContactInfoView contactInfoView;

    public ContactInfoModule(BookingPresenter.ContactInfoView contactInfoView) {
        this.contactInfoView = contactInfoView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(contactInfoView, bus);
    }
}
