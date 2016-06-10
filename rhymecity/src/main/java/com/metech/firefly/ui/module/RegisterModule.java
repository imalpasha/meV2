package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.Register.RegisterFragment;
import com.metech.firefly.ui.presenter.RegisterPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = RegisterFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class RegisterModule {

    private final RegisterPresenter.RegisterView registerView;

    public RegisterModule(RegisterPresenter.RegisterView registerView) {
        this.registerView = registerView;
    }

    @Provides
    @Singleton
    RegisterPresenter provideLoginPresenter(Bus bus) {
        return new RegisterPresenter(registerView, bus);
    }
}
