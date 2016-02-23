package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.Login.LoginFragment;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = LoginFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class LoginModule {

    private final LoginPresenter.LoginView loginView;

    public LoginModule(LoginPresenter.LoginView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter(Bus bus) {
        return new LoginPresenter(loginView, bus);
    }
}
