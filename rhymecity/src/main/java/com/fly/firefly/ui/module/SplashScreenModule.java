package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.SplashScreen.SplashScreenFragment;
import com.fly.firefly.ui.presenter.HomePresenter;
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
