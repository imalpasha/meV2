package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.Login.LoginFragment;
import com.metech.firefly.ui.presenter.LoginPresenter;
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
