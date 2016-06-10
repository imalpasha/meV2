package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.SplashScreen.SplashScreenFragment;
import com.metech.firefly.ui.presenter.HomePresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SplashScreenFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SplashScreenModule {

    private final HomePresenter.SplashScreen loginView;

    public SplashScreenModule(HomePresenter.SplashScreen loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    HomePresenter provideLoginPresenter(Bus bus) {
        return new HomePresenter(loginView, bus);
    }
}
