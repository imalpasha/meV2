package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.Aboutus.AboutUsFragment;
import com.metech.firefly.ui.presenter.AboutUsPresenter;
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
