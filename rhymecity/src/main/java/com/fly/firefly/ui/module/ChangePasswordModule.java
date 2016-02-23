package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.PasswordExpired.ChangePasswordFragment;
import com.fly.firefly.ui.presenter.ChangePasswordPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ChangePasswordFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ChangePasswordModule {

    private final ChangePasswordPresenter.ChangePasswordView ChangePasswordview;

    public ChangePasswordModule(ChangePasswordPresenter.ChangePasswordView ChangePasswordview) {
        this.ChangePasswordview = ChangePasswordview;
    }

    @Provides
    @Singleton
    ChangePasswordPresenter provideChangePasswordPresenter(Bus bus) {
        return new ChangePasswordPresenter(ChangePasswordview, bus);
    }
}
