package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.BookingFlight.ContactInfoFragment;
import com.metech.firefly.ui.presenter.BookingPresenter;
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
