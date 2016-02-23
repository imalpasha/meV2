package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.Terms.TermsFragment;
import com.fly.firefly.ui.presenter.TermsPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = TermsFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class TermsModule {

    private final TermsPresenter.TermsView Termsview;

    public TermsModule(TermsPresenter.TermsView Termsview) {
        this.Termsview = Termsview;
    }

    @Provides
    @Singleton
    TermsPresenter provideTermsPresenter(Bus bus) {
        return new TermsPresenter(Termsview, bus);
    }
}
