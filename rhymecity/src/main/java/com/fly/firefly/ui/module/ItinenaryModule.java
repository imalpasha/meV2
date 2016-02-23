package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BookingFlight.ItinenaryFragment;
import com.fly.firefly.ui.activity.Login.LoginFragment;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ItinenaryFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ItinenaryModule {

    private final BookingPresenter.ItinenaryView loginView;

    public ItinenaryModule(BookingPresenter.ItinenaryView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    BookingPresenter provideLoginPresenter(Bus bus) {
        return new BookingPresenter(loginView, bus);
    }
}
