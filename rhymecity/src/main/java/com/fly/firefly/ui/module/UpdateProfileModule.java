package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.UpdateProfile.UpdateProfileFragment;
import com.fly.firefly.ui.presenter.UpdateProfilePresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = UpdateProfileFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class UpdateProfileModule {

    private final UpdateProfilePresenter.UpdateProfileView UpdateProfileview;

    public UpdateProfileModule(UpdateProfilePresenter.UpdateProfileView UpdateProfileview) {
        this.UpdateProfileview = UpdateProfileview;
    }

    @Provides
    @Singleton
    UpdateProfilePresenter provideUpdateProfilePresenter(Bus bus) {
        return new UpdateProfilePresenter(UpdateProfileview, bus);
    }
}
