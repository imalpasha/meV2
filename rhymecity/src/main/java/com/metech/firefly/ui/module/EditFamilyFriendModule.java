package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.BookingFlight.ContactInfoFragment;
import com.metech.firefly.ui.activity.BookingFlight.ManageFamilyAndFriend.EditFamilyFriendsFragment;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = EditFamilyFriendsFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class EditFamilyFriendModule {

    private final BookingPresenter.EditFamilyFriendView editFamilyFriendView;

    public EditFamilyFriendModule(BookingPresenter.EditFamilyFriendView editFamilyFriendView) {
        this.editFamilyFriendView = editFamilyFriendView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(editFamilyFriendView, bus);
    }
}
