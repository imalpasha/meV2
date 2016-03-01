package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.Aboutus.AboutUsFragment;
import com.fly.firefly.ui.presenter.AboutUsPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = AboutUsFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class AboutUsModule {

    private final AboutUsPresenter.AboutUsView aboutUsView;

    public AboutUsModule(AboutUsPresenter.AboutUsView aboutUsView) {
        this.aboutUsView = aboutUsView;
    }

    @Provides
    @Singleton
    AboutUsPresenter provideAboutUsPresenter(Bus bus) {
        return new AboutUsPresenter(aboutUsView, bus);
    }
}
